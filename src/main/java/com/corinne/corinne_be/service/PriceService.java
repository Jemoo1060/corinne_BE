package com.corinne.corinne_be.service;

import com.corinne.corinne_be.dto.candle_dto.DatePageDto;
import com.corinne.corinne_be.dto.candle_dto.DateReponseDto;
import com.corinne.corinne_be.dto.candle_dto.MinutePageDto;
import com.corinne.corinne_be.dto.socket_dto.PricePublishingDto;
import com.corinne.corinne_be.exception.CustomException;
import com.corinne.corinne_be.exception.ErrorCode;
import com.corinne.corinne_be.model.DayCandle;
import com.corinne.corinne_be.model.MinuteCandle;
import com.corinne.corinne_be.model.User;
import com.corinne.corinne_be.repository.BookmarkRepository;
import com.corinne.corinne_be.repository.DayCandleRepository;
import com.corinne.corinne_be.repository.MinuteCandleRepository;
import com.corinne.corinne_be.repository.RedisRepository;
import com.corinne.corinne_be.utils.TikerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PriceService {

    private final MinuteCandleRepository minuteCandleRepository;
    private final DayCandleRepository dateCandleRepository;
    private final BookmarkRepository bookmarkRepository;
    private final RedisRepository redisRepository;
    private final TikerUtil tikerUtil;
    List<String> tikers = Arrays.asList("KRW-BTC", "KRW-SOL", "KRW-ETH", "KRW-XRP", "KRW-ADA", "KRW-AVAX", "KRW-DOT", "KRW-MATIC");

    @Autowired
    public PriceService(MinuteCandleRepository minuteCandleRepository, DayCandleRepository dateCandleRepository,
                        BookmarkRepository bookmarkRepository, RedisRepository redisRepository, TikerUtil tikerUtil) {
        this.minuteCandleRepository = minuteCandleRepository;
        this.dateCandleRepository = dateCandleRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.redisRepository = redisRepository;
        this.tikerUtil = tikerUtil;
    }

    // 분봉 조회
    @Transactional
    public ResponseEntity<List<MinutePageDto>> minuteCandleList(String tikerName) {

        if(!tikers.contains(tikerName)){
            throw new CustomException(ErrorCode.NON_EXIST_TIKER);
        }

        List<MinuteCandle> entites = minuteCandleRepository.findAllByTiker(tikerName);
        List<MinutePageDto> minutePageDtos = new ArrayList<>();

        for(MinuteCandle minuteCandle : entites){
            String tiker = minuteCandle.getTiker();
            int startPrice = minuteCandle.getStartPrice();
            int endPrice = minuteCandle.getEndPrice();
            int highPrice = minuteCandle.getHighPrice();
            int lowPrice = minuteCandle.getLowPrice();
            String date = Integer.toString(minuteCandle.getTradeDate());

            SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat newDtFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date formatDate = null;
            try {
                formatDate = dtFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String tradeDate  = newDtFormat.format(formatDate);

            int time = minuteCandle.getTradeTime()+900;
            if(time/100 >= 24){
                time-=2400;
            }

            String tradeTime = String.format("%02d:%02d", time/100, time%100);

            minutePageDtos.add(new MinutePageDto(tiker,startPrice,endPrice,highPrice,lowPrice,tradeDate,tradeTime));
        }
        return new ResponseEntity<>(minutePageDtos, HttpStatus.OK);
    }

    // 일봉 조회
    @Transactional
   public ResponseEntity<List<DatePageDto>> dateCandleList(String tikerName) {

        if(!tikers.contains(tikerName)){
            throw new CustomException(ErrorCode.NON_EXIST_TIKER);
        }

        List<DayCandle> entites = dateCandleRepository.findAllByTikerOrderByTradeDateAsc(tikerName);
        List<DatePageDto> dateCandles = new ArrayList<>();
        for(DayCandle dayCandle : entites){

            String tiker = dayCandle.getTiker();
            int startPrice = dayCandle.getStartPrice();
            int endPrice = dayCandle.getEndPrice();
            int highPrice = dayCandle.getHighPrice();
            int lowPrice = dayCandle.getLowPrice();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
            String date = simpleDateFormat.format(dayCandle.getTradeDate());

            dateCandles.add(new DatePageDto(tiker, startPrice, endPrice, highPrice, lowPrice, date));
        }
       return new ResponseEntity<>(dateCandles, HttpStatus.OK);
    }


    // 일별 등락률 랭킹
    @Transactional
    public ResponseEntity<List<DateReponseDto>> dateRankList(User user) {
        List<DateReponseDto> dateReponseDtos = new ArrayList<>();
        for(String tiker : tikers){
            PricePublishingDto pricePublishingDto = redisRepository.getTradePrice(tiker);

            String tikername = tikerUtil.switchTiker(tiker);

            // 어제 일봉 종료값
            int endPrice = pricePublishingDto.getPrevClosingPrice();
            float fluctuationRate = pricePublishingDto.getSignedChangeRate();

            // 코인단위
            String unit = tiker.substring(4);

            // 즐겨찾기 유무
            boolean favorite = bookmarkRepository.existsByUserIdAndTiker(user.getUserId(), tiker);

            DateReponseDto dateReponseDto = new DateReponseDto(tiker,tikername, endPrice, fluctuationRate,unit,favorite);

            dateReponseDtos.add(dateReponseDto);
        }

        // 등락률에 맞춰 정렬
        dateReponseDtos = dateReponseDtos.stream().sorted(Comparator.comparing(DateReponseDto::getFluctuationRate).reversed()).collect(Collectors.toList());


        return new ResponseEntity<>(dateReponseDtos,HttpStatus.OK);
    }

    public ResponseEntity<PricePublishingDto> tradePrice(String tiker) {

        if(!tikers.contains(tiker)){
            throw new CustomException(ErrorCode.NON_EXIST_TIKER);
        }
        return new ResponseEntity<>(redisRepository.getTradePrice(tiker), HttpStatus.OK);
    }
}
