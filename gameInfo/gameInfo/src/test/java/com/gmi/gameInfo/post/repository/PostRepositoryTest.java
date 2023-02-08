package com.gmi.gameInfo.post.repository;

import com.gmi.gameInfo.post.domain.Post;
import com.gmi.gameInfo.post.domain.dto.PostDto;
import com.gmi.gameInfo.post.exception.NotFoundPostException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;
    
    @Test
    @Rollback
    @DisplayName("포스트 저장 테스트")
    void savePost() {
    
        //given
        PostDto postDto = PostDto.builder()
                .title("테스트")
                .content("테스트 컨텐트")
                .build();
    
        //when
        Post post = Post.createPostByDto(postDto);
        Post save = postRepository.save(post);

        //then
        assertEquals(post.getTitle(), save.getTitle());
        assertEquals(post.getContent(), save.getContent());
    }

    @Test
    @Rollback
    @DisplayName("단일조회 - 실패")
    void failFindById() {
    
        //given
        PostDto postDto = PostDto.builder()
                .title("테스트")
                .content("테스트 컨텐트")
                .build();

        Post post = Post.createPostByDto(postDto);
        Post save = postRepository.save(post);

        //when
        Optional<Post> find = postRepository.findById(0L);
        
        //then
        assertThrows(NotFoundPostException.class, () -> {
            find.orElseThrow(NotFoundPostException::new);
        });
    }
    
    @Test
    @DisplayName("단일조회 - 성공")
    void findById() {

        //given
        PostDto postDto = PostDto.builder()
                .title("테스트")
                .content("테스트 컨텐트")
                .build();

        Post post = Post.createPostByDto(postDto);
        Post save = postRepository.save(post);

        //when
        Optional<Post> find = postRepository.findById(save.getId());

        //then
        assertThat(find.orElse(null)).isNotNull();
        assertEquals(find.get().getTitle(), save.getTitle());
        assertEquals(find.get().getContent(), save.getContent());
    }

    @Test
    @DisplayName("삭제 - 성공")
    void PostRepositoryTest() {

        //given
        PostDto postDto = PostDto.builder()
                .title("테스트")
                .content("테스트 컨텐트")
                .build();

        Post post = Post.createPostByDto(postDto);
        Post save = postRepository.save(post);

        //when
        postRepository.delete(save);

        //then
        assertThrows(NotFoundPostException.class, () -> {
            postRepository.findById(save.getId()).orElseThrow(NotFoundPostException::new);
        });

    }
}
