package com.gmi.gameInfo.news.repository;

import com.gmi.gameInfo.main.dto.NewsImageListDto;
import com.gmi.gameInfo.main.dto.NewsSimpleDto;
import com.gmi.gameInfo.news.domain.dto.NewsDto;
import com.gmi.gameInfo.news.domain.dto.NewsListDto;
import com.gmi.gameInfo.news.domain.dto.NewsSearchDto;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface NewsCustomRepository {

    List<NewsListDto> findListByPageable(NewsSearchDto searchDto, Pageable pageable) throws ParseException;

    Optional<NewsDto> findDtoOneById(Long id);

    List<NewsImageListDto> findNewsImageListAtMain() throws ParseException;

    List<NewsSimpleDto> findNewsListByNotIds(List<Long> ids);
}
