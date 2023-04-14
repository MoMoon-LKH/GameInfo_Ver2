package com.gmi.gameInfo.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.post.domain.dto.PostListDto;
import com.gmi.gameInfo.post.domain.dto.PostSearchDto;
import com.gmi.gameInfo.post.domain.dto.PostVo;
import com.gmi.gameInfo.member.service.MemberService;
import com.gmi.gameInfo.post.domain.Post;
import com.gmi.gameInfo.post.domain.dto.PostDto;
import com.gmi.gameInfo.post.service.PostService;
import com.gmi.gameInfo.post.service.PostServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.xml.transform.Result;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                .andExpect(status().isCreated())
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
    
    
    @Test
    @WithMockUser
    @DisplayName("게시글 업데이트")
    void updatePost() throws Exception {
    
        //given
        PostDto postDto = PostDto.builder()
                .title("update")
                .content("updateC").build();

        Member member = createTestMember();
        Post post = createTestPost(member);
        Post updatePost = createTestPost(member);

        given(memberService.findByLoginId(any())).willReturn(member);
        given(postService.findById(post.getId())).willReturn(post);
        doNothing().when(postService).updatePost(post, postDto);
        updatePost.updatePost(postDto);
        given(postService.findPostVoById(post.getId())).willReturn(convertPostVo(updatePost));

        //when
        ResultActions result = mockMvc.perform(patch("/api/post/" + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postDto))
                .with(csrf())
        );
        
        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(postDto.getTitle()))
                .andExpect(jsonPath("$.content").value(postDto.getContent()));
    }

    @Test
    @WithMockUser
    @DisplayName("게시글 삭제")
    void deletePost() throws Exception {

        //given
        Member member = createTestMember();
        Post post = createTestPost(member);

        given(memberService.findByLoginId(anyString())).willReturn(member);
        given(postService.findById(anyLong())).willReturn(post);
        given(postService.checkPostOwner(any(Post.class), any(Member.class))).willReturn(true);
        given(postService.deleteOneById(post)).willReturn(true);

        //when
        ResultActions result = mockMvc.perform(delete("/api/post/" + 1L)
                .with(csrf()));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("true"));


    }
    
    @Test
    @WithMockUser
    @DisplayName("카테고리에 따른 게시글 리스트 조회 - 성공")
    void findAllByCategoryIdAndPage() throws Exception {
    
        //given
        Long categoryId = 1L;
        Pageable page = PageRequest.of(0, 10);
        List<PostListDto> list = new ArrayList<>();
        list.add(PostListDto.builder()
                .postId(1L)
                .title("title")
                .memberId(1L)
                .nickname("nickname")
                .createDate(new Date())
                .build()
        );
        list.add(PostListDto.builder()
                .postId(2L)
                .title("title2")
                .memberId(1L)
                .nickname("nickname")
                .createDate(new Date())
                .build()
        );


        given(postService.findListByCategoryIdAndPage(any(PostSearchDto.class), any(Pageable.class))).willReturn(list);

        //when
        ResultActions result = mockMvc.perform(get("/api/post/list/" + categoryId)
                .content(objectMapper.writeValueAsString(page)));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(list)));
        ;
    }

    private Member createTestMember() {
        return Member.builder()
                .id(1L)
                .loginId("test")
                .build();
    }

    private Post createTestPost(Member member) {
        return Post.builder()
                .id(1L)
                .title("test")
                .content("testContent")
                .member(member)
                .createDate(new Date())
                .build();
    }

    private PostVo convertPostVo(Post post) {
        return PostVo.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .memberId(post.getMember().getId())
                .nickname(post.getMember().getNickname())
                .build();

    }
}
