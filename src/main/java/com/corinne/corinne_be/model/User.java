package com.corinne.corinne_be.model;

import com.corinne.corinne_be.dto.user_dto.UserRequestdto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "tbl_user")
public class User {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    @Column
    private Long userId;

    @Column
    private String imageUrl = "null";

    @Column(nullable = false, unique = true)
    private String userEmail;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Long accountBalance = 1000000L;

    @Column(nullable = false)
    private int exp = 0;

    @Column(nullable = false)
    private boolean firstLogin = true;

    @Column(nullable = false)
    private double lastFluctuation;

    @Column(nullable = false)
    private int lastRank;

    @Column(nullable = false)
    private int highRank;

    @Column(nullable = false)
    private boolean alarm = false;

    @Column(nullable = false)
    private Long rival = 0L;

    @Version
    private Integer version;

    public User(String nickname, String password, String userEmail) {
        this.nickname = nickname;
        this.password = password;
        this.userEmail = userEmail;
    }

    //회원정보 수정
    public void infoUpdate(UserRequestdto userRequestdto){
        this.nickname = userRequestdto.getNickname();
        this.firstLogin = false;
    }


    //프로필 이미지 수정
    public void profileImgUpdate(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public void balanceUpdate(Long accountBalance) {
        this.accountBalance = accountBalance;
    }

    public void lastFluctuationUpdate(double lastFluctuation){
        this.lastFluctuation = lastFluctuation;
    }

    public void rivalUpdate(Long rivalId){
        this.rival = rivalId;
    }

    public void expUpdate(int exp){
        this.exp += exp;
    }

    public void addBalance(Long reword){
        this.accountBalance += reword;
    }
}


