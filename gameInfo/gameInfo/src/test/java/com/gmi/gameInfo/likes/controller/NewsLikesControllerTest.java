package com.gmi.gameInfo.likes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmi.gameInfo.likes.domain.LikeType;
import com.gmi.gameInfo.likes.domain.NewsLikes;
import com.gmi.gameInfo.likes.service.NewsLikesService;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NewsLikesController.class)
public class NewsLikesControllerTest {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    NewsLikesService newsLikesService;

    @MockBean
    NewsService newsService;

    @MockBean
    MemberService memberService;

    private Member member = Member.builder()
            .id(1L)
            .loginId("test")
            .password("test")
            .email("test")
            .name("test")
            .birthday(new Date())
            .nickname("test")
            .phoneNo("01012341234")
            .roleType(RoleType.USER)
            .build();

    private News news = News.builder()
            .id(1L)
            .title("test")
            .content("content")
            .member(member)
            .build();


    @Test
    @WithMockUser
    @DisplayName("NewsLike like 버튼 - 존재 X")
    void pushNewsLike_Empty() throws Exception {
    
        //given
        given(memberService.findByLoginId(any())).willReturn(member);
        given(newsService.findById(any())).willReturn(news);
        given(newsLikesService.findByNewsIdAndMemberId(any(), any())).willReturn(Optional.ofNullable(null));
        given(newsLikesService.countByNewsIdAndLikesType(any(), any())).willReturn(1);
    
        //when
        ResultActions result = mockMvc.perform(post("/api/news/likes/" + 1)
                        .param("type", "like")
                        .with(csrf())
        );


        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.like").value(1));

    }

    @Test
    @WithMockUser
    @DisplayName("NewsLike like 버튼 - 이미 like가 존재하는 경우")
    void pushNewsLike_AlreadyExists() throws Exception {

        //given
        NewsLikes like = NewsLikes.createNewLikes(news, member, LikeType.LIKE);
        given(memberService.findByLoginId(any())).willReturn(member);
        given(newsService.findById(any())).willReturn(news);
        given(newsLikesService.findByNewsIdAndMemberId(any(), any())).willReturn(Optional.of(like));
        given(newsLikesService.countByNewsIdAndLikesType(any(), any())).willReturn(0);

        //when
        ResultActions result = mockMvc.perform(post("/api/news/likes/" + 1)
                .param("type", "like")
                .with(csrf())
        );


        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.like").value(0));
    }
    
    @Test
    @WithMockUser
    @DisplayName("NewsLike like - 이미 dislike가 존재하는 경우")
    void pushNewsLike_AlreadyExistsDislike() throws Exception {
    
        //given
        NewsLikes like = NewsLikes.createNewLikes(news, member, LikeType.DISLIKE);
        given(memberService.findByLoginId(any())).willReturn(member);
        given(newsService.findById(any())).willReturn(news);
        given(newsLikesService.findByNewsIdAndMemberId(any(), any())).willReturn(Optional.of(like));
        given(newsLikesService.countByNewsIdAndLikesType(any(), eq(LikeType.LIKE))).willReturn(1);
        given(newsLikesService.countByNewsIdAndLikesType(any(), eq(LikeType.DISLIKE))).willReturn(0);

        //when
        ResultActions result = mockMvc.perform(post("/api/news/likes/" + 1)
                .param("type", "dislike")
                .with(csrf())
        );

        //then
        result
                .andDo(print())
                .andExpect(status().isOk());
    }
}
