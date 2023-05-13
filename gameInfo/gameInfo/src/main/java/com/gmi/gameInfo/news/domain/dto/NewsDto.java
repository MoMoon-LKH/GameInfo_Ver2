package com.gmi.gameInfo.news.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date createDate;


    private MemberSimpleDto memberDto;

    private PlatformDto platformDto;

}
