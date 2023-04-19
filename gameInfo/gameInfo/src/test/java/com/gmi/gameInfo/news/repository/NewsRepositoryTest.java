package com.gmi.gameInfo.news.repository;

import com.gmi.gameInfo.config.TestConfig;
import com.gmi.gameInfo.news.domain.News;
import com.gmi.gameInfo.news.exception.NotFoundNewsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Rollback
@Import(TestConfig.class)
public class NewsRepositoryTest {

    @Autowired
    NewsRepository newsRepository;
    
    
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


}
