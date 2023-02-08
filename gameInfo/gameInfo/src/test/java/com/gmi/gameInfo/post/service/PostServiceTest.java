package com.gmi.gameInfo.post.service;

import com.gmi.gameInfo.post.domain.Post;
import com.gmi.gameInfo.post.domain.dto.PostDto;
import com.gmi.gameInfo.post.exception.FailDeletePostException;
import com.gmi.gameInfo.post.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    
    @Test
    @DisplayName("포스트 등록")
    void savePost() {
    
        //given
        PostDto postDto = PostDto.builder()
                .title("test")
                .content("test")
                .build();

        Post post = Post.createPostByDto(postDto);

        given(postRepository.save(post)).willReturn(post);

        //when
        Post save = postService.save(post);

        //then
        assertEquals(post.getContent(), save.getContent());
    }

    @Test
    @DisplayName("포스트 삭제 - 실패")
    void failDeletePost() {

        //given
        Post post = Post.builder()
                .title("test")
                .content("content")
                .createDate(new Date())
                .id(1L).build();
        given(postRepository.deletePostById(post.getId())).willReturn(0);

        //when

        //then
        assertThrows(FailDeletePostException.class, () -> {
            postService.deleteOneById(post);
        });
    }

    @Test
    @DisplayName("포스트 삭제")
    void deletePost() {

        //given
        Post post = Post.builder()
                .title("test")
                .content("content")
                .createDate(new Date())
                .id(1L).build();

        given(postRepository.deletePostById(post.getId())).willReturn(1);

        //when
        boolean bool = postService.deleteOneById(post);

        //then
        assertTrue(bool);
    }


}
