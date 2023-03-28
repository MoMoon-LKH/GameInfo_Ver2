package com.gmi.gameInfo.post.repository;

import com.gmi.gameInfo.category.domain.Category;
import com.gmi.gameInfo.category.repository.CategoryRepository;
import com.gmi.gameInfo.config.TestConfig;
import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.domain.RoleType;
import com.gmi.gameInfo.member.repository.MemberRepository;
import com.gmi.gameInfo.post.domain.Post;
import com.gmi.gameInfo.post.domain.dto.PostListDto;
import com.gmi.gameInfo.post.domain.dto.PostVo;
import com.gmi.gameInfo.post.exception.NotFoundPostException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestConfig.class)
@Rollback
public class PostCustomRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CategoryRepository categoryRepository;

    private Member member;
    private Category category;

    @BeforeEach
    public void setUp(){
        member = Member.builder()
                .loginId("test")
                .birthday(new Date())
                .nickname("test")
                .name("name")
                .email("email")
                .password("password")
                .roleType(RoleType.USER)
                .build();
        memberRepository.save(member);

        category = Category.builder()
                .name("test")
                .build();
        categoryRepository.save(category);
    }
    
    @Test
    @DisplayName("PostVo 단일 조회 - 실패")
    void findPostVoById_Fail() {
    
        //given
        Long id = 1L;
    
        //when
        Optional<PostVo> find = postRepository.findPostVoById(id);

        //then
        assertThrows(NotFoundPostException.class, () -> {
            PostVo postVo = find.orElseThrow(NotFoundPostException::new);
        });
    }
    
    
    @Test
    @DisplayName("PostVo 단일 조회 - 성공")
    void findPostVoById_Success() {
    
        //given
        Post post = postRepository.save(
                Post.builder()
                        .title("test")
                        .content("content")
                        .createDate(new Date())
                        .member(member)
                        .build()
        );

        //when
        PostVo postVo = postRepository.findPostVoById(post.getId()).orElseThrow(NotFoundPostException::new);
        
        //then
        assertEquals(post.getId(), postVo.getId());
    }

    @Test
    @DisplayName("PostListDto 리스트 조회")
    void findAllByCategoryIdAndPage() {

        //given
        Pageable pageable = PageRequest.of(0, 10);
        Long categoryId = 1L;

        Post post = Post.builder()
                .title("test")
                .content("test")
                .createDate(new Date())
                .category(category)
                .member(member).build();
        postRepository.save(post);

        //when
        List<PostListDto> postList = postRepository.findAllByCategoryIdAndPage(categoryId, pageable);

        //then
        assertEquals(1, postList.size());

    }
}
