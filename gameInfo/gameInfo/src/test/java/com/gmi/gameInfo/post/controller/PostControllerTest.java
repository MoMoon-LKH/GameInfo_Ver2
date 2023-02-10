package com.gmi.gameInfo.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.post.domain.dto.PostVo;
import com.gmi.gameInfo.member.service.MemberService;
import com.gmi.gameInfo.post.domain.Post;
import com.gmi.gameInfo.post.domain.dto.PostDto;
import com.gmi.gameInfo.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@ActiveProfiles("dev")
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("게시글 등록 API")
    void createTest() throws Exception {

        //given
        PostDto postDto = PostDto.builder()
                        .title("test")
                        .content("test").build();
        Member member = Member.builder()
                        .id(1L)
                        .loginId("test")
                        .build();

        Post post = Post.builder()
                .id(1L)
                .title("test")
                .content("test")
                .member(member).build();

        PostVo vo = PostVo.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .memberId(member.getId())
                .nickname(member.getNickname()).build();

        given(postService.save(any(Post.class))).willReturn(post);
        given(memberService.findByLoginId(any())).willReturn(member);
        given(postService.findPostVoById(any())).willReturn(vo);

        //when

        //then
        mockMvc.perform(post("/api/post/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDto))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()));
    }


    @Test
    @WithMockUser
    @DisplayName("단일조회(Vo) API ")
    void findPostVoById() throws Exception {

        //given
        PostVo vo = PostVo.builder()
                .id(1L)
                .title("test")
                .content("testContent")
                .memberId(1L)
                .nickname("test").build();

        given(postService.findPostVoById(1L)).willReturn(vo);

        //when
        ResultActions result = mockMvc.perform(get("/api/post/" + vo.getId()));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(vo.getTitle()))
                .andExpect(jsonPath("$.content").value(vo.getContent()));

    }
}
