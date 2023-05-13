package com.gmi.gameInfo.news.repository;

import com.gmi.gameInfo.config.TestConfig;
import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.domain.RoleType;
import com.gmi.gameInfo.member.repository.MemberRepository;
import com.gmi.gameInfo.news.domain.News;
import com.gmi.gameInfo.news.domain.dto.NewsDto;
import com.gmi.gameInfo.news.domain.dto.NewsListDto;
import com.gmi.gameInfo.news.domain.dto.NewsSearchDto;
import com.gmi.gameInfo.news.exception.NotFoundNewsException;
import com.gmi.gameInfo.platform.domain.Platform;
import com.gmi.gameInfo.platform.repository.PlatformRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Rollback
@Import(TestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NewsRepositoryTest {

    @Autowired
    NewsRepository newsRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PlatformRepository platformRepository;

    private Member member;

    private Member member2;

    private Platform platform;

    @BeforeAll
    void setUp() {
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

        member2 = Member.builder()
                .loginId("test123")
                .birthday(new Date())
                .nickname("findNickname")
                .name("name")
                .email("email123")
                .password("password")
                .roleType(RoleType.USER)
                .build();
        memberRepository.save(member2);

        platform = Platform.builder()
                .name("platform1")
                .build();
        platformRepository.save(platform);
    }
    
    @Test
    @DisplayName("News 저장 테스트")
    void saveTest() {
    
        //given
        News news = News.builder()
                .title("title")
                .content("content")
                .build();
    
        //when
        News save = newsRepository.save(news);

        //then
        assertSame(save, news);
    }
    
    @Test
    @DisplayName("News 단일 조회")
    void findByIdTest() {
    
        //given
        News news = News.builder()
                .title("title")
                .content("content")
                .build();
        newsRepository.save(news);
    
        //when
        News save = newsRepository.findById(news.getId()).orElseGet(null);

        //then
        assertSame(news, save);
    }

    @Test
    @DisplayName("News 삭제")
    void deleteTest() {

        //given
        News news = News.builder()
                .title("title")
                .content("content")
                .build();
        newsRepository.save(news);

        //when
        newsRepository.delete(news);

        //then
        assertThrows(NotFoundNewsException.class, () -> {
            newsRepository.findById(news.getId()).orElseThrow(NotFoundNewsException::new);
        });
    }

    
    @Test
    @DisplayName("NewsListDto 리스트 조회")
    void findListByPageable() {
    
        //given
        News news = News.builder()
                .title("title")
                .content("content")
                .member(member)
                .build();

        newsRepository.save(news);

        Pageable pageable = PageRequest.of(0, 20);

        NewsSearchDto searchDto = NewsSearchDto.builder()
                .platformId(0L)
                .searchSelect("")
                .searchWord("")
                .build();
    
        //when
        List<NewsListDto> find = newsRepository.findListByPageable(searchDto, pageable);

        //then
        assertEquals(1, find.size());

    }
    
    
    @Test
    @DisplayName("NewsLisDto 리스트 조회 - platform 조회")
    void findListByPageable_Platform() {
    
        //given
        News news = News.builder()
                .title("title")
                .content("content")
                .member(member)
                .build();

        News news2 = News.builder()
                .title("title")
                .content("cotent")
                .member(member)
                .platform(platform)
                .build();

        newsRepository.save(news);
        newsRepository.save(news2);

        Pageable pageable = PageRequest.of(0, 20);

        NewsSearchDto searchDto = NewsSearchDto.builder()
                .platformId(platform.getId())
                .searchSelect("")
                .searchWord("")
                .build();

        //when
        List<NewsListDto> find = newsRepository.findListByPageable(searchDto, pageable);
    

        //then
        assertEquals(1, find.size());

    }

    @Test
    @DisplayName("NewsListDto 리스트 조회 - title 검색 조회")
    void findListByPageable_SearchTitle() {

        News news = News.builder()
                .title("title")
                .content("content")
                .member(member)
                .platform(platform)
                .build();

        News news2 = News.builder()
                .title("find")
                .content("content")
                .member(member)
                .platform(platform)
                .build();

        newsRepository.save(news);
        newsRepository.save(news2);

        Pageable pageable = PageRequest.of(0, 20);

        NewsSearchDto searchDto = NewsSearchDto.builder()
                .platformId(platform.getId())
                .searchSelect("title")
                .searchWord("fin")
                .build();

        //when
        List<NewsListDto> find = newsRepository.findListByPageable(searchDto, pageable);

        //then
        assertEquals(1, find.size());
    }

    @Test
    @DisplayName("NewsListDto 리스트 조회 - writer 검색 조회")
    void findListByPageable_SearchWriter() {

        //given
        News news = News.builder()
                .title("title")
                .content("content")
                .member(member)
                .platform(platform)
                .build();

        News news2 = News.builder()
                .title("find")
                .content("content")
                .member(member2)
                .platform(platform)
                .build();

        newsRepository.save(news);
        newsRepository.save(news2);

        Pageable pageable = PageRequest.of(0, 20);

        NewsSearchDto searchDto = NewsSearchDto.builder()
                .platformId(platform.getId())
                .searchSelect("writer")
                .searchWord("find")
                .build();

        //when
        List<NewsListDto> find = newsRepository.findListByPageable(searchDto, pageable);

        //then
        assertEquals(1, find.size());
    }
    
    
    @Test
    @DisplayName("NewsDto 단일조회")
    void findOneById() {
    
        //given
        News news = News.builder()
                .title("title")
                .content("content")
                .createDate(new Date())
                .member(member)
                .build();

        News save = newsRepository.save(news);
        System.out.println("save.getId() = " + save.getId());

        //when
        NewsDto find = newsRepository.findDtoOneById(save.getId()).orElseGet(null);


        //then
        assertSame(news.getId(), find.getId());

    }

    @Test
    @DisplayName("News 총 개수 조회")
    void countAll() {

        //given
        News news = News.builder()
                .title("title")
                .content("content")
                .createDate(new Date())
                .member(member)
                .platform(platform)
                .build();
        News news2 = News.builder()
                .title("title")
                .content("content")
                .createDate(new Date())
                .member(member)
                .build();
        newsRepository.save(news);
        newsRepository.save(news2);

        //when
        int count = newsRepository.countAllBy();

        //then
        assertEquals(2, count);
    }

    @Test
    @DisplayName("News 총 개수 조회 - platformId")
    void countByPlatformId() {

        //given
        News news = News.builder()
                .title("title")
                .content("content")
                .createDate(new Date())
                .member(member)
                .platform(platform)
                .build();
        News news2 = News.builder()
                .title("title")
                .content("content")
                .createDate(new Date())
                .member(member)
                .build();
        newsRepository.save(news);
        newsRepository.save(news2);

        //when
        int count = newsRepository.countByPlatformId(platform.getId());

        //then
        assertEquals(1, count);
    }

}
