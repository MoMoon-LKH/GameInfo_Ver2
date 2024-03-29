package com.gmi.gameInfo.news.service;


import com.gmi.gameInfo.main.dto.NewsImageListDto;
import com.gmi.gameInfo.main.dto.NewsSimpleDto;
import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.news.domain.News;
import com.gmi.gameInfo.news.domain.dto.NewsCreateDto;
import com.gmi.gameInfo.news.domain.dto.NewsDto;
import com.gmi.gameInfo.news.domain.dto.NewsListDto;
import com.gmi.gameInfo.news.domain.dto.NewsSearchDto;
import com.gmi.gameInfo.news.exception.NotFoundNewsException;
import com.gmi.gameInfo.news.repository.NewsRepository;
import com.gmi.gameInfo.platform.domain.Platform;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        newsService.deleteRecord(news);

        //then
        assertThrows(NotFoundNewsException.class, () -> {
            News find = newsService.findById(1L);
        });
    }

    @Test
    @DisplayName("NewsListDto 리스트 조회")
    void findListByPageable() throws ParseException {

        //given
        Pageable pageable = PageRequest.of(0, 10);
        NewsSearchDto newsSearchDto = NewsSearchDto.builder()
                .platformId(0L)
                .build();

        List<NewsListDto> newsList = new ArrayList<>();
        newsList.add(
                NewsListDto.builder()
                        .id(1L)
                        .title("title")
                        .nickname("nickname")
                        .createDate(new Date())
                        .build()
        );

        given(newsRepository.findListByPageable(any(), any())).willReturn(newsList);

        //when
        List<NewsListDto> list = newsService.findListByPageable(newsSearchDto, pageable);

        //then
        assertEquals(1, list.size());
    }


    @Test
    @DisplayName("NewsDto 단일조회 - 실패")
    void findOneById_Fail() {

        //given
        given(newsRepository.findDtoOneById(any(Long.class))).willReturn(Optional.ofNullable(null));

        //when


        //then
        assertThrows(NotFoundNewsException.class, () -> {
            NewsDto find = newsService.findDtoOneById(1L);
        });

    }


    @Test
    @DisplayName("NewsDto 단일조회 - 성공")
    void findOneById_Success() {

        //given
        NewsDto newsDto = NewsDto.builder()
                .id(1L)
                .title("title")
                .content("content")
                .createDate(new Date())
                .build();

        given(newsRepository.findDtoOneById(any(Long.class))).willReturn(Optional.of(newsDto));

        //when
        NewsDto find = newsService.findDtoOneById(1L);

        //then
        assertEquals(1, find.getId());
    }
    
    @Test
    @DisplayName("News 업데이트")
    void updateNews() {
    
        //given
        Platform platform1 = Platform.builder()
                .id(1L)
                .name("p1")
                .build();
        Platform platform2 = Platform.builder()
                .id(2L)
                .name("p2")
                .build();

        News news = News.builder()
                .id(1L)
                .title("title")
                .content("content")
                .createDate(new Date())
                .platform(platform1)
                .build();

        NewsCreateDto createDto = NewsCreateDto.builder()
                .title("updateTitle")
                .content("updateContent")
                .platformId(2L)
                .build();

        //when
        newsService.updateNews(news, createDto, platform2);
        
        //then
        assertEquals(createDto.getTitle(), news.getTitle());
        assertSame(platform2, news.getPlatform());
    }
    
    @Test
    @DisplayName("NewsDto 단일 조회")
    void findDtoById() {
    
        //given
        Member member = Member.builder()
                .id(1L)
                .nickname("nick")
                .build();
        Platform platform = Platform.builder()
                .id(1L)
                .name("platform")
                .build();

        News news = News.builder()
                .id(1L)
                .title("title")
                .content("content")
                .createDate(new Date())
                .member(member)
                .platform(platform)
                .build();

        given(newsRepository.findById(1L)).willReturn(Optional.of(news));
    
        //when
        NewsDto newsDto = newsService.findDtoById(1L);

        //then
        assertEquals(news.getId(), newsDto.getId());
    }
    
    @Test
    @DisplayName("NewsImageListDto 조회 - 메인화면 이미지 리스트")
    void findMainList() throws ParseException {
    
        //given
        List<NewsImageListDto> list = new ArrayList<>();
        NewsImageListDto dto1 = NewsImageListDto.builder()
                .id(1L)
                .title("title1")
                .imageName("test1.jpg")
                .build();
        NewsImageListDto dto2 = NewsImageListDto.builder()
                .id(1L)
                .title("title1")
                .imageName("test1.jpg")
                .build();
        list.add(dto1);
        list.add(dto2);

        given(newsRepository.findNewsImageListAtMain()).willReturn(list);

        //when
        List<NewsImageListDto> find = newsService.findNewsImageListAtMain();

        //then
        assertEquals(2, find.size());
    }
    
    @Test
    @DisplayName("NewsSimpleDto 리스트 조회 - ids 제외")
    void findNewsSimpleByNotInIds() {
    
        //given
        List<NewsSimpleDto> list = new ArrayList<>();
        NewsSimpleDto dto = NewsSimpleDto.builder()
                .id(1L)
                .title("title1")
                .build();

        NewsSimpleDto dto2 = NewsSimpleDto.builder()
                .id(2L)
                .title("title2")
                .build();

        list.add(dto);
        list.add(dto2);

        List<Long> ids = new ArrayList<>();
        ids.add(3L);

        given(newsRepository.findNewsListByNotIds(ids)).willReturn(list);

        //when
        List<NewsSimpleDto> result = newsService.findNewsSimpleListByNotInIds(ids);

        //then
        assertEquals(2, result.size());

    }


    @Test
    @DisplayName("delete 상태 업데이트")
    void updateDeleteYn() {

        //given
        News news = News.builder()
                .id(1L)
                .title("title")
                .content("content")
                .build();

        //when
        newsService.delete(news);

        //then
        assertTrue(news.isDeleteYn());
    }





}
