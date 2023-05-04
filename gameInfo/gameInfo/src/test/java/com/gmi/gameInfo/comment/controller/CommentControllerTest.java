package com.gmi.gameInfo.comment.controller;

import com.gmi.gameInfo.comment.domain.dto.CommentDto;
import com.gmi.gameInfo.comment.service.CommentService;
import com.gmi.gameInfo.member.service.MemberService;
import com.gmi.gameInfo.news.service.NewsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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


    @Test
    @WithMockUser
    @DisplayName("댓글 리스트 조회 - 뉴스")
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
}
