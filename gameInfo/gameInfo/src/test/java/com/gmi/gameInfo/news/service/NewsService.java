package com.gmi.gameInfo.news.service;

import com.gmi.gameInfo.news.domain.News;
import com.gmi.gameInfo.news.domain.dto.NewsCreateDto;
import com.gmi.gameInfo.news.domain.dto.NewsDto;
import com.gmi.gameInfo.news.domain.dto.NewsListDto;
import com.gmi.gameInfo.news.domain.dto.NewsSearchDto;
import com.gmi.gameInfo.news.exception.NotFoundNewsException;
import com.gmi.gameInfo.news.repository.NewsRepository;
import com.gmi.gameInfo.platform.domain.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    @Transactional
    public News save(News news) {
        return newsRepository.save(news);
    }


    public News findById(Long id) {
        return newsRepository.findById(id).orElseThrow(NotFoundNewsException::new);
    }

    @Transactional
    public void delete(News news) {
        newsRepository.delete(news);
    }

    @Transactional
    public void updateNews(News news, NewsCreateDto dto, Platform platform) {
        news.updateNews(dto, platform);
    }

    public List<NewsListDto> findListByPageable(NewsSearchDto newsSearchDto, Pageable pageable) {
        return newsRepository.findListByPageable(newsSearchDto, pageable);
    }

    public NewsDto findDtoOneById(Long id) {
        return newsRepository.findDtoOneById(id).orElseThrow(NotFoundNewsException::new);
    }
}
