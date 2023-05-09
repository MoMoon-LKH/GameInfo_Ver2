package com.gmi.gameInfo.news.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsListDto {

    private Long id;

    private String title;

    private int views;

    private int likesCount;

    private int commentCount;

    private Long memberId;

    private String nickname;

    private String createDate;
}
