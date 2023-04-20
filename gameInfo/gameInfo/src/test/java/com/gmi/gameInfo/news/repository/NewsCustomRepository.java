package com.gmi.gameInfo.news.repository;

import com.gmi.gameInfo.news.domain.dto.NewsListDto;
import com.gmi.gameInfo.news.domain.dto.NewsSearchDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NewsCustomRepository {

    List<NewsListDto> findListByPageable(NewsSearchDto searchDto, Pageable pageable);

}
