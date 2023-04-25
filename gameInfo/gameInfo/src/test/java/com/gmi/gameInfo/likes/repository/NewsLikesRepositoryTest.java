package com.gmi.gameInfo.likes.repository;

import com.gmi.gameInfo.config.TestConfig;
import com.gmi.gameInfo.likes.domain.LikesType;
import com.gmi.gameInfo.likes.domain.NewsLikes;
import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.domain.RoleType;
import com.gmi.gameInfo.member.repository.MemberRepository;
import com.gmi.gameInfo.news.domain.News;
import com.gmi.gameInfo.news.exception.NotFoundNewsException;
import com.gmi.gameInfo.news.repository.NewsRepository;
import com.gmi.gameInfo.post.repository.PostRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestConfig.class)
@Rollback
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NewsLikesRepositoryTest {

    @Autowired
    NewsLikesRepository likesRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    NewsRepository newsRepository;


    private Member member;

    private News news;

    @BeforeAll
    void setUp(){
        member = Member.builder()
                .loginId("test")
                .name("test")
                .email("test123")
                .password("testtest123")
                .nickname("test")
                .birthday(new Date())
                .phoneNo("01012341234")
                .roleType(RoleType.USER)
                .createDate(new Date())
                .build();
        memberRepository.save(member);

        news = News.builder()
                .title("title")
                .content("content")
                .member(member)
                .createDate(new Date())
                .build();
        newsRepository.save(news);
    }

    @Test
    @DisplayName("NewsLikes 저장")
    void likesSaveTest() {

        //given
        NewsLikes likes = NewsLikes.createNewLikes(news, member, LikesType.LIKE);

        //when
        NewsLikes save = likesRepository.save(likes);
        News find = newsRepository.findById(news.getId()).orElse(null);

        //then
        assertEquals(LikesType.LIKE, save.getLikesType());
        assertEquals(1, find.getLikes().size());

    }


    @Test
    @DisplayName("NewsLikes 삭제")
    void NewsLikesRepositoryTest() {

        //given
        NewsLikes likes = NewsLikes.createNewLikes(news, member, LikesType.LIKE);

        //when
        NewsLikes save = likesRepository.save(likes);
        News findNews = newsRepository.findById(news.getId()).orElse(null);
        findNews.getLikes().remove(likes);
        likesRepository.delete(likes);

        NewsLikes find = likesRepository.findById(likes.getId()).orElse(null);

        //then
        assertNull(find);
        assertEquals(0, findNews.getLikes().size());

    }
    
    @Test
    @DisplayName("NewsLikes 조회 - newsId, memberId")
    void findByNewsIdAndMemberId() {

        //given
        NewsLikes likes = NewsLikes.createNewLikes(news, member, LikesType.LIKE);
        likesRepository.save(likes);

        //when
        Optional<NewsLikes> find = likesRepository.findByNewsIdAndMemberId(news.getId(), member.getId());

        //then
        assertSame(likes, find.get());

    }
}
