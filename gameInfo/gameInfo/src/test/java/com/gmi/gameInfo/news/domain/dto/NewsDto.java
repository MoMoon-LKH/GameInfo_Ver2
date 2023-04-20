package com.gmi.gameInfo.news.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsDto {

    private Long id;

    private String title;

    private String content;

    private int commentCnt;

    private Date createDate;

    private Long memberId;

    private String nickname;

}
