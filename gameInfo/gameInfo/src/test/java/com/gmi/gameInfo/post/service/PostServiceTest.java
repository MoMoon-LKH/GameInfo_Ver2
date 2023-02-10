package com.gmi.gameInfo.post.service;

import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.post.domain.dto.PostVo;
import com.gmi.gameInfo.post.domain.Post;
import com.gmi.gameInfo.post.domain.dto.PostDto;
import com.gmi.gameInfo.post.exception.FailDeletePostException;
import com.gmi.gameInfo.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    private PostService postService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        postService = new PostServiceImpl(postRepository);
    }

    @Test
    @DisplayName("포스트 등록")
    void savePost() {
    
        //given
        PostDto postDto = PostDto.builder()
                .title("test")
                .content("test")
                .build();

        Member member = createMember();

        Post post = Post.createPostByDto(postDto, member);

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
    
    @Test
    @DisplayName("게시글 업데이트")
    void updatePost() {

        //given
        PostDto postDto = PostDto.builder()
                .title("test")
                .content("test")
                .build();

        PostDto updateDto = PostDto.builder()
                .title("updateTest")
                .content("updateTest")
                .build();

        Member member = createMember();

        Post post = Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .id(1L)
                .createDate(new Date()).build();

        given(postRepository.save(post)).willReturn(post);
        Post save = postService.save(post);

        //when
        postService.updatePost(save, updateDto);

        //then
        assertEquals(updateDto.getTitle(), save.getTitle());
        assertEquals(updateDto.getContent(), save.getContent());

    }

    @Test
    @DisplayName("게시글 단일조회(Vo)")
    void findVoById() {
        PostVo postVo = PostVo.builder()
                .id(1L)
                .title("test")
                .content("test")
                .memberId(1L)
                .nickname("nickname")
                .createDate(new Date())
                .build();

        given(postRepository.findPostVoById(1L)).willReturn(Optional.ofNullable(postVo));


        //when
        PostVo post = postService.findPostVoById(1L);

        //then
        assertEquals(post.getTitle(), postVo.getTitle());
        assertEquals(post.getMemberId(), postVo.getMemberId());
    }

    private Member createMember() {
        return Member.builder()
                .loginId("test")
                .name("테스트")
                .nickname("테스트 닉네임")
                .phoneNo("01012345678")
                .email("test@asdf.com").build();
    }
}
