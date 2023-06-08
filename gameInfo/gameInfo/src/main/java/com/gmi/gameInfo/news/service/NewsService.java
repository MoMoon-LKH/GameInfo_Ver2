package com.gmi.gameInfo.news.service;

import com.gmi.gameInfo.main.dto.NewsImageListDto;
import com.gmi.gameInfo.main.dto.NewsSimpleDto;
import com.gmi.gameInfo.member.domain.dto.MemberSimpleDto;
import com.gmi.gameInfo.news.domain.News;
import com.gmi.gameInfo.news.domain.dto.NewsCreateDto;
import com.gmi.gameInfo.news.domain.dto.NewsDto;
import com.gmi.gameInfo.news.domain.dto.NewsListDto;
import com.gmi.gameInfo.news.domain.dto.NewsSearchDto;
import com.gmi.gameInfo.news.exception.NotFoundNewsException;
import com.gmi.gameInfo.news.repository.NewsRepository;
import com.gmi.gameInfo.platform.domain.Platform;
import com.gmi.gameInfo.platform.domain.dto.PlatformDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
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

    public NewsDto findDtoById(Long id) {
        News news = newsRepository.findById(id).orElseThrow(NotFoundNewsException::new);
        PlatformDto platformDto = PlatformDto.builder()
                .id(news.getPlatform().getId())
                .name(news.getPlatform().getName())
                .build();
        MemberSimpleDto memberSimpleDto = MemberSimpleDto.builder()
                .id(news.getMember().getId())
                .nickname(news.getMember().getNickname())
                .build();

        return NewsDto.builder()
                .id(news.getId())
                .title(news.getTitle())
                .content(news.getContent())
                .createDate(news.getCreateDate())
                .memberDto(memberSimpleDto)
                .platformDto(platformDto)
                .build();
    }

    @Transactional
    public void delete(News news) {
        newsRepository.delete(news);
    }

    @Transactional
    public void updateNews(News news, NewsCreateDto dto, Platform platform) {
        news.updateNews(dto, platform);
    }

    @Transactional
    public void updateViews(News news) {
        news.updateViews();
    }

    public List<NewsListDto> findListByPageable(NewsSearchDto newsSearchDto, Pageable pageable) throws ParseException {
        return newsRepository.findListByPageable(newsSearchDto, pageable);
    }

    public NewsDto findDtoOneById(Long id) {
        return newsRepository.findDtoOneById(id).orElseThrow(NotFoundNewsException::new);
    }

    public int countByPlatformId(Long id) {
        if(id != 0) {
            return newsRepository.countByPlatformId(id);
        } else {
            return newsRepository.countAllBy();
        }
    }

    public List<NewsImageListDto> findNewsImageListAtMain() throws ParseException {
        return newsRepository.findNewsImageListAtMain();
    }

    public List<NewsSimpleDto> findNewsSimpleListByNotInIds(List<Long> ids) {
        return newsRepository.findNewsListByNotIds(ids);
    }
}
