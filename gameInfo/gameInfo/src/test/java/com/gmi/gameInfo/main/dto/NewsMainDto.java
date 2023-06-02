package com.gmi.gameInfo.main.dto;

import com.gmi.gameInfo.news.domain.dto.NewsListDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsMainDto {

    private List<NewsImageListDto> newsImageList;

    private List<NewsSimpleDto> newsList;
}
