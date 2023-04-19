package com.gmi.gameInfo.news.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsCreateDto {

    private String title;
    private String content;

    private Long platformId;

    private List<Long> imageIds;
}
