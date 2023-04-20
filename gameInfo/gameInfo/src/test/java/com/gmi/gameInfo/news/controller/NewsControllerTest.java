package com.gmi.gameInfo.news.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmi.gameInfo.image.domain.Images;
import com.gmi.gameInfo.image.service.ImagesService;
import com.gmi.gameInfo.member.service.MemberService;
import com.gmi.gameInfo.news.domain.News;
import com.gmi.gameInfo.news.domain.dto.NewsCreateDto;
import com.gmi.gameInfo.news.domain.dto.NewsListDto;
import com.gmi.gameInfo.news.domain.dto.NewsSearchDto;
import com.gmi.gameInfo.news.service.NewsService;
import com.gmi.gameInfo.platform.domain.Platform;
import com.gmi.gameInfo.platform.service.PlatformService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NewsController.class)
@ActiveProfiles("dev")
public class NewsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    NewsService newsService;

    @MockBean
    ImagesService imagesService;

    @MockBean
    PlatformService platformService;

    @MockBean
    MemberService memberService;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @WithMockUser
    @DisplayName("News 생성 API")
    void createApiTest() throws Exception {

        //given
        NewsCreateDto createDto = NewsCreateDto.builder()
                .title("title")
                .content("content")
                .platformId(1L)
                .build();

        Platform platform = Platform.builder()
                .id(1L)
                .name("title")
                .build();

        News news = News.builder()
                .id(1L)
                .title("title")
                .content("content")
                .platform(platform)
                .build();

        given(platformService.findById(any())).willReturn(platform);
        given(newsService.save(any(News.class))).willReturn(news);

        //when
        ResultActions result = mockMvc.perform(post("/api/news/create")
                .content(objectMapper.writeValueAsString(createDto))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
        );

        //then
        result
                .andDo(print())
                .andExpect(status().isOk());

    }
    
    @Test
    @WithMockUser
    @DisplayName("News 생성 API - 이미지 포함")
    void createApiTest_IncludeImages() throws Exception {
    
        //given
        List<Long> images = new ArrayList<>();
        images.add(1L);
        images.add(2L);

        NewsCreateDto createDto = NewsCreateDto.builder()
                .title("title")
                .content("content")
                .platformId(1L)
                .imageIds(images)
                .build();

        Images images1 = Images.builder()
                .id(1L)
                .path("/images/images1.jpg")
                .fileName("images1")
                .extension(".jpg")
                .build();

        Images images2 = Images.builder()
                .id(2L)
                .path("/images/images2.jpg")
                .fileName("images2")
                .extension(".jpg")
                .build();

        Platform platform = Platform.builder()
                .id(1L)
                .name("title")
                .build();

        News news = News.builder()
                .id(1L)
                .title("title")
                .content("content")
                .platform(platform)
                .build();

        given(platformService.findById(any())).willReturn(platform);
        given(newsService.save(any(News.class))).willReturn(news);
        given(imagesService.findById(1L)).willReturn(images1);
        given(imagesService.findById(2L)).willReturn(images2);
    
        //when
        ResultActions result = mockMvc.perform(post("/api/news/create")
                .content(objectMapper.writeValueAsString(createDto))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
        );

        //then
        result
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser
    @DisplayName("NewsListDto 리스트 조회")
    void findListByPageable() throws Exception {

        //given
        NewsSearchDto newsSearchDto = NewsSearchDto.builder()
                .platformId(1L)
                .searchWord("")
                .searchSelect("")
                .build();

        List<NewsListDto> newsList = new ArrayList<>();
        newsList.add(
                NewsListDto.builder()
                        .id(1L)
                        .title("title")
                        .memberId(1L)
                        .nickname("nickname")
                        .build()
        );

        given(newsService.findListByPageable(any(), any())).willReturn(newsList);

        //when
        ResultActions result = mockMvc.perform(get("/api/news/list?page=0&size=20")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newsSearchDto))
                .with(csrf())
        );

        //then
        result
                .andDo(print())
                .andExpect(status().isOk());
    }

}
