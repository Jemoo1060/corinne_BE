package com.corinne.corinne_be.service;

import com.corinne.corinne_be.dto.account_dto.AccountResponseDto;
import com.corinne.corinne_be.dto.account_dto.AccountSimpleDto;
import com.corinne.corinne_be.dto.account_dto.CoinsDto;
import com.corinne.corinne_be.dto.transaction_dto.TransactionDto;
import com.corinne.corinne_be.exception.CustomException;
import com.corinne.corinne_be.exception.ErrorCode;
import com.corinne.corinne_be.model.*;
import com.corinne.corinne_be.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    private final CoinRepository coinRepository;
    private final UserRepository userRepository;
    private final RedisRepository redisRepository;
    private final TransactionRepository transactionRepository;
    private final BookmarkRepository bookmarkRepository;
    private final QuestRepository questRepository;

    @Autowired
    public AccountService(CoinRepository coinRepository, UserRepository userRepository,
                          RedisRepository redisRepository, TransactionRepository transactionRepository,
                          BookmarkRepository bookmarkRepository, QuestRepository questRepository) {
        this.coinRepository = coinRepository;
        this.userRepository = userRepository;
        this.redisRepository = redisRepository;
        this.transactionRepository = transactionRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.questRepository = questRepository;
    }


    // 보유 자산
    @Transactional
    public ResponseEntity<AccountResponseDto> getBalance(User user) {

        // 사용 가능한 포인트
        Long accountBalance = user.getAccountBalance();

        // 총  보유 코인 재산
        Long totalCoinBalance = 0L;
        // 보유중인 코인 리스트
        List<Coin> haveCoins = coinRepository.findAllByUser_UserId(user.getUserId());

        // 보유중인 코인 정보 리스트
        List<CoinsDto> coins = new ArrayList<>();

        List<Long> coinBalances = new ArrayList<>();

        for (Coin coin : haveCoins) {

            String tiker = coin.getTiker();

            // 살 당시 코인 현재가
            BigDecimal buyPrice = BigDecimal.valueOf(coin.getBuyPrice());
            // 현재가
            BigDecimal currentPrice = BigDecimal.valueOf(redisRepository.getTradePrice(coin.getTiker()).getTradePrice());
            // 래버리지
            BigDecimal leverage = BigDecimal.valueOf(coin.getLeverage());
            // 구매 총금액
            BigDecimal amount = BigDecimal.valueOf(coin.getAmount());
            // 래버리지 적용한 수익률
            BigDecimal fluctuationRate = currentPrice.subtract(buyPrice).multiply(leverage).divide(buyPrice, 2, RoundingMode.HALF_UP);
            // 현재 해당 코인의 가치
            Long coinBalance = fluctuationRate.add(BigDecimal.valueOf(1)).multiply(amount).setScale(0, RoundingMode.CEILING).longValue();

            totalCoinBalance += coinBalance;
            coinBalances.add(coinBalance);

            CoinsDto coinsDto = new CoinsDto(tiker, buyPrice.doubleValue(), currentPrice.intValue(), leverage.intValue(), fluctuationRate.doubleValue() * 100, coinBalance, coinBalance - coin.getAmount());
            coins.add(coinsDto);
        }

        Long totalBalance = totalCoinBalance + accountBalance;

        // 수익률 계산
        BigDecimal temp = new BigDecimal(totalBalance - 1000000);
        BigDecimal rateCal = new BigDecimal(10000);
        double fluctuationRate = temp.divide(rateCal, 2, RoundingMode.HALF_EVEN).doubleValue();


        for (int i = 0; i < coins.size(); i++) {
            long balance = coinBalances.get(i);
            // 원그래프 비중 계산
            BigDecimal importanceRateCal = new BigDecimal(balance * 100);
            double importanceRate = importanceRateCal.divide(new BigDecimal(totalCoinBalance), 2, RoundingMode.HALF_EVEN).doubleValue();
            coins.get(i).setImportanceRate(importanceRate);
        }

        return new ResponseEntity<>(new AccountResponseDto(user.getLastFluctuation(), accountBalance, totalBalance, fluctuationRate, coins), HttpStatus.OK);
    }


    // 모의투자페이지 자산
    @Transactional
    public ResponseEntity<AccountSimpleDto> getSimpleBalance(String tiker, User user) {

        List<Coin> coin = coinRepository.findByTikerAndUser_UserId(tiker, user.getUserId());

        Long accountBalance = user.getAccountBalance();
        return new ResponseEntity<>(new AccountSimpleDto(accountBalance, coin), HttpStatus.OK);
    }

    // 보유 자산 리셋
    @Transactional
    public ResponseEntity<HttpStatus> resetAccount(User user) {

        user.balanceUpdate(1000000L);
        userRepository.save(user);

        // 보유 코인 지우기
        coinRepository.deleteAllByUser_UserId(user.getUserId());

        // 리셋 내역 추가
        TransactionDto transactionDto = new TransactionDto(user, "reset", 0, 1000000L, "reset", 1, 0L);
        Transaction transaction = new Transaction(transactionDto);
        transactionRepository.save(transaction);
        redisRepository.resetBankruptcy(user.getUserId());

        Quest quest = questRepository.findByUser_UserIdAndQuestNo(user.getUserId(), 9).orElse(null);

        if (quest != null) {
            if (!quest.isClear()) {
                quest.update(true);
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 즐겨찾기 등록
    @Transactional
    public ResponseEntity<HttpStatus> inputBookmark(String tiker, User user) {

        if (bookmarkRepository.existsByUserIdAndTiker(user.getUserId(), tiker)) {
            throw new CustomException(ErrorCode.EXIST_BOOKMARK);
        }

        Quest quest = questRepository.findByUser_UserIdAndQuestNo(user.getUserId(), 1).orElse(null);

        if (quest != null) {
            if (!quest.isClear()) {
                quest.update(true);
            }
        }

        // 즐겨찾기 등록
        Bookmark bookmark = new Bookmark(user.getUserId(), tiker);
        bookmarkRepository.save(bookmark);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 즐겨찾기 삭제
    @Transactional
    public ResponseEntity<HttpStatus> deleteBookmark(String tiker, User user) {

        Bookmark bookmark = bookmarkRepository.findByUserIdAndTiker(user.getUserId(), tiker).orElse(null);

        if (bookmark == null) {
            throw new CustomException(ErrorCode.NON_EXIST_BOOKMARK);
        }

        bookmarkRepository.delete(bookmark);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}


















