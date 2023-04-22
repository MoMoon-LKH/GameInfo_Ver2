package com.gmi.gameInfo.news.repository;

import com.gmi.gameInfo.news.domain.dto.NewsDto;
import com.gmi.gameInfo.news.domain.dto.NewsListDto;
import com.gmi.gameInfo.news.domain.dto.NewsSearchDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface NewsCustomRepository {

    List<NewsListDto> findListByPageable(NewsSearchDto searchDto, Pageable pageable);

    Optional<NewsDto> findDtoOneById(Long id);

}
