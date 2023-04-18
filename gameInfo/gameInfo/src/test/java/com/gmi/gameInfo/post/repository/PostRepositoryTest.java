package com.gmi.gameInfo.post.repository;

import com.gmi.gameInfo.config.TestConfig;
import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.post.domain.dto.PostVo;
import com.gmi.gameInfo.member.repository.MemberRepository;
import com.gmi.gameInfo.post.domain.Post;
import com.gmi.gameInfo.post.domain.dto.PostDto;
import com.gmi.gameInfo.post.exception.NotFoundPostException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestConfig.class)
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;


    @Test
    @Rollback
    @DisplayName("포스트 저장 테스트")
    void savePost() {
    
        //given
        PostDto postDto = PostDto.builder()
                .title("테스트")
                .content("테스트 컨텐트")
                .build();
        Member member = createMember();
    
        //when
        Post post = Post.createPostByDto(postDto, member);
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

        Member member = createMember();

        Post post = Post.createPostByDto(postDto, member);
        Post save = postRepository.save(post);

        //when
        Optional<Post> find = postRepository.findById(0L);
        
        //then
        assertThrows(NotFoundPostException.class, () -> {
            find.orElseThrow(NotFoundPostException::new);
        });
    }
    
    @Test
    @Rollback
    @DisplayName("단일조회 - 성공")
    void findById() {

        //given
        PostDto postDto = PostDto.builder()
                .title("테스트")
                .content("테스트 컨텐트")
                .build();
        Member member = createMember();

        Post post = Post.createPostByDto(postDto, member);
        Post save = postRepository.save(post);

        //when
        Optional<Post> find = postRepository.findById(save.getId());

        //then
        assertThat(find.orElse(null)).isNotNull();
        assertEquals(find.get().getTitle(), save.getTitle());
        assertEquals(find.get().getContent(), save.getContent());
    }

    @Test
    @Rollback
    @DisplayName("삭제 - 성공")
    void PostRepositoryTest() {

        //given
        PostDto postDto = PostDto.builder()
                .title("테스트")
                .content("테스트 컨텐트")
                .build();
        Member member = createMember();

        Post post = Post.createPostByDto(postDto, member);
        Post save = postRepository.save(post);

        //when
        postRepository.delete(save);

        //then
        assertThrows(NotFoundPostException.class, () -> {
            postRepository.findById(save.getId()).orElseThrow(NotFoundPostException::new);
        });

    }
    
    
    @Test
    @Rollback
    @DisplayName("단일 조회(Vo 반환) - 실패")
    void failFindPostVoById() {
    
        //given
        PostDto postDto = PostDto.builder()
                .title("테스트")
                .content("테스트 컨텐트")
                .build();
        Member member = createMember();
        memberRepository.save(member);

        Post post = Post.createPostByDto(postDto, member);
        Post save = postRepository.save(post);

        //when
        Optional<PostVo> find = postRepository.findPostVoById(0L);

        //then
        assertThrows(NotFoundPostException.class, () -> {
            find.orElseThrow(NotFoundPostException::new);
        });
    }

    @Test
    @Rollback
    @DisplayName("단일 조회(Vo 반환) - 성공")
    void findByPostVoById() {
        //given
        PostDto postDto = PostDto.builder()
                .title("테스트")
                .content("테스트 컨텐트")
                .build();
        Member member = createMember();
        memberRepository.save(member);

        Post post = Post.createPostByDto(postDto, member);
        Post save = postRepository.save(post);

        //when
        PostVo find = postRepository
                .findPostVoById(save.getId())
                .orElseThrow(NotFoundPostException::new);

        //then
        assertEquals(save.getId(), find.getId());
        assertEquals(save.getMember().getId(), find.getMemberId());
    }


    private Member createMember() {
        Member member = Member.builder()
                .loginId("test")
                .name("테스트")
                .password("test")
                .nickname("테스트 닉네임")
                .phoneNo("01012345678")
                .birthday(new Date())
                .email("test@asdf.com").build();
        return member;
    }

}
