package com.gmi.gameInfo.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmi.gameInfo.comment.domain.Comment;
import com.gmi.gameInfo.comment.domain.dto.CommentCreateDto;
import com.gmi.gameInfo.comment.domain.dto.CommentDto;
import com.gmi.gameInfo.comment.service.CommentService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CommentService commentService;

    @MockBean
    NewsService newsService;

    @MockBean
    MemberService memberService;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @WithMockUser
    @DisplayName("댓글 리스트 조회 API - 뉴스")
    void commentListByPage_News() throws Exception {

        //given
        Long newsId = 1L;

        List<CommentDto> list = new ArrayList<>();

        CommentDto dto1 = CommentDto.builder()
                .id(1L)
                .content("test")
                .groups(0)
                .sequence(0)
                .memberId(1L)
                .nickname("test")
                .build();
        list.add(dto1);

        given(commentService.countByNewsId(any(Long.class))).willReturn(1);
        given(commentService.findListPageByNewsId(any(Long.class), any(Pageable.class))).willReturn(list);

        //when
        ResultActions result = mockMvc.perform(get("/api/comment/news/" + newsId + "?size=30&page=0"));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.list[0].id").value(1L))
                .andExpect(jsonPath("$.list[0].content").value("test"))
        ;
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 생성 API - 뉴스")
    void createComment() throws Exception {

        //given
        CommentCreateDto createDto = CommentCreateDto.builder()
                .content("content")
                .group(0)
                .postId(1L)
                .memberId(1L)
                .build();
        News news = News.builder()
                .id(1L)
                .title("test")
                .content("test")
                .build();
        Member member = Member.builder()
                .id(1L)
                .loginId("test")
                .password("test")
                .name("test")
                .birthday(new Date())
                .email("test")
                .roleType(RoleType.USER)
                .build();
        Comment comment = Comment.createNewsComment(createDto, member, news);

        given(newsService.findById(any(Long.class))).willReturn(news);
        given(memberService.findByLoginId(any(String.class))).willReturn(member);
        given(commentService.maxGroupsByNewsId(any(Long.class))).willReturn(0);
        given(commentService.save(any(Comment.class))).willReturn(comment);
        given(commentService.countByNewsId(any(Long.class))).willReturn(1);


        //when
        ResultActions result = mockMvc.perform(post("/api/comment/news")
                .content(objectMapper.writeValueAsString(createDto))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
        );

        //then
        result
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.page").value(0));

    }
    
    @Test
    @WithMockUser
    @DisplayName("대댓글 생성 API - 뉴스")
    void createReplyComment() throws Exception {

        //given
        CommentCreateDto createDto = CommentCreateDto.builder()
                .content("content")
                .group(0)
                .postId(1L)
                .memberId(1L)
                .build();
        News news = News.builder()
                .id(1L)
                .title("test")
                .content("test")
                .build();
        Member member = Member.builder()
                .id(1L)
                .loginId("test")
                .password("test")
                .name("test")
                .birthday(new Date())
                .email("test")
                .roleType(RoleType.USER)
                .build();
        Comment comment = Comment.createNewsComment(createDto, member, news);

        given(newsService.findById(any(Long.class))).willReturn(news);
        given(memberService.findByLoginId(any(String.class))).willReturn(member);
        given(commentService.maxSequenceByNewsIdAndGroups(any(Long.class), any(Integer.class))).willReturn(1);
        given(commentService.save(any(Comment.class))).willReturn(comment);
        given(commentService.countByNewsId(any(Long.class))).willReturn(1);


        //when
        ResultActions result = mockMvc.perform(post("/api/comment/news")
                .content(objectMapper.writeValueAsString(createDto))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
        );

        //then
        result
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.page").value(0));

    }
  
    
    
    @Test
    @DisplayName("댓글 내용 변경")
    @WithMockUser
    void updateCommentContent() throws Exception {
    
        //given
        CommentCreateDto createDto = CommentCreateDto.builder()
                .content("content")
                .group(0)
                .sequence(0)
                .postId(1L)
                .memberId(1L)
                .build();
        News news = News.builder()
                .id(1L)
                .title("test")
                .content("test")
                .build();
        Member member = Member.builder()
                .id(1L)
                .loginId("test")
                .password("test")
                .name("test")
                .birthday(new Date())
                .email("test")
                .roleType(RoleType.USER)
                .build();
        Comment comment = Comment.createNewsComment(createDto, member, news);
        String updateContent = "update";

        given(commentService.findById(any())).willReturn(comment);
        given(memberService.findByLoginId(any())).willReturn(member);

    
        //when
        ResultActions result = mockMvc.perform(patch("/api/comment/" + 1L)
                .content("{\"content\":\"" + updateContent + "\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
        );


        //then
        result
                .andDo(print())
                .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("댓글 삭제")
    @WithMockUser
    void deleteComment() throws Exception{
    
        //given
        CommentCreateDto createDto = CommentCreateDto.builder()
                .content("content")
                .group(0)
                .sequence(0)
                .postId(1L)
                .memberId(1L)
                .build();
        News news = News.builder()
                .id(1L)
                .title("test")
                .content("test")
                .build();
        Member member = Member.builder()
                .id(1L)
                .loginId("test")
                .password("test")
                .name("test")
                .birthday(new Date())
                .email("test")
                .roleType(RoleType.USER)
                .build();
        Comment comment = Comment.createNewsComment(createDto, member, news);
        String updateContent = "update";

        given(commentService.findById(any())).willReturn(comment);
        given(memberService.findByLoginId(any())).willReturn(member);
    
        //when
        ResultActions result = mockMvc.perform(delete("/api/comment/" + 1L)
                .with(csrf())
        );

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }


}
