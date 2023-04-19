package com.gmi.gameInfo.news.service;


import com.gmi.gameInfo.news.domain.News;
import com.gmi.gameInfo.news.exception.NotFoundNewsException;
import com.gmi.gameInfo.news.repository.NewsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class NewsServiceTest {

    @Mock
    NewsRepository newsRepository;

    @InjectMocks
    NewsService newsService;
    
    
    @Test
    @DisplayName("저장 테스트")
    void newsSave() {
    
        //given
        News news = News.builder()
                .id(1L)
                .title("title")
                .content("content")
                .build();
        given(newsRepository.save(any(News.class))).willReturn(news);

        //when
        News save = newsService.save(news);

        //then
        assertEquals(1L, save.getId());
        assertEquals("title",save.getTitle());
        assertEquals("content", save.getContent());
    }

    @Test
    @DisplayName("단일 조회 - 실패")
    void findById_Fail() {

        //given

        given(newsRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(null));

        //when

        //then
        assertThrows(NotFoundNewsException.class, () -> {
           newsService.findById(1L);
        });
    }

    @Test
    @DisplayName("단일 조회 - 성공")
    void findById_Success() {
    
        //given
        News news = News.builder()
                .id(1L)
                .title("title")
                .content("content")
                .build();

        given(newsRepository.findById(any(Long.class))).willReturn(Optional.of(news));

        //when
        News find = newsService.findById(1L);

        //then
        assertEquals(1L, find.getId());
    }

    @Test
    @DisplayName("News 삭제")
    void deleteNews() {

        //given
        News news = News.builder()
                .id(1L)
                .title("title")
                .content("content")
                .build();

        doNothing().when(newsRepository).delete(any());
        given(newsRepository.findById(any())).willReturn(Optional.ofNullable(null));

        //when
        newsService.delete(news);

        //then
        assertThrows(NotFoundNewsException.class, () -> {
            News find = newsService.findById(1L);
        });
    }

}
