package com.gmi.gameInfo.news.domain.dto;

import com.gmi.gameInfo.member.domain.dto.MemberSimpleDto;
import com.gmi.gameInfo.platform.domain.dto.PlatformDto;
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

    private Date createDate;


    private MemberSimpleDto memberDto;

    private PlatformDto platformDto;

}
