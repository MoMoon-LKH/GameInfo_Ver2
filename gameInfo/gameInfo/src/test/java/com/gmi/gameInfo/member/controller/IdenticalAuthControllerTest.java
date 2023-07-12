package com.gmi.gameInfo.member.controller;

import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.domain.RoleType;
import com.gmi.gameInfo.member.service.MemberService;
import com.gmi.gameInfo.news.domain.News;
import com.gmi.gameInfo.news.service.NewsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(IdenticalAuthController.class)
public class IdenticalAuthControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private NewsService newsService;

    @Test
    @WithMockUser
    @DisplayName("News 권한 확인 - 성공")
    void getNewsAuth_Success() throws Exception {

        //given
        Member member = Member.builder()
                .id(1L)
                .name("test")
                .email("test@test.com")
                .password("test123")
                .roleType(RoleType.USER)
                .build();

        News news = News.builder()
                .id(1L)
                .title("title")
                .content("content")
                .member(member)
                .build();

        given(memberService.findByLoginId(any(String.class))).willReturn(member);
        given(newsService.findById(any(Long.class))).willReturn(news);

        //when
        ResultActions result = mockMvc.perform(get("/api/auth/news/1")
                .with(csrf())
        );


        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @WithMockUser
    @DisplayName("News 권한 확인 - No Permission Exception")
    void getNewsAuth_Exception() throws Exception {

        //given
        Member member = Member.builder()
                .id(1L)
                .name("test")
                .email("test@test.com")
                .password("test123")
                .roleType(RoleType.USER)
                .build();

        Member loginUser = Member.builder()
                .id(2L)
                .name("test1")
                .email("test1@test.com")
                .password("test123")
                .roleType(RoleType.USER)
                .build();

        News news = News.builder()
                .id(1L)
                .title("title")
                .content("content")
                .member(member)
                .build();

        given(memberService.findByLoginId(any(String.class))).willReturn(loginUser);
        given(newsService.findById(any(Long.class))).willReturn(news);

        //when
        ResultActions result = mockMvc.perform(get("/api/auth/news/1")
                .with(csrf())
        );

        //then
        result
                .andDo(print())
                .andExpect(status().isForbidden());

    }
}
