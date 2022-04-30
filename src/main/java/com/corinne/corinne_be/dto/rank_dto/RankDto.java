package com.corinne.corinne_be.dto.rank_dto;

import lombok.Getter;

import java.util.List;

@Getter
public class RankDto {

    private Long userId;
    private String nickname;
    private String imageUrl;
    private Long totalBalance;
    private double fluctuationRate;
    private boolean follow;

    public RankDto() {
    }

    public RankDto(Long userId, String nickname, String imageUrl, Long totalBalance, double fluctuationRate, boolean follow) {
        this.userId = userId;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.totalBalance = totalBalance;
        this.fluctuationRate = fluctuationRate;
        this.follow = follow;
    }
}
