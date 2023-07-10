package com.gmi.gameInfo.news.service;


import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.news.domain.News;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("dev")
public class NewsServiceSpringTest {

    @Autowired
    private NewsService newsService;


    @Test
    @DisplayName("단일 조회 - 조회수 업데이트 동시성 확인")
    void findById_ConcurrencyTest() throws InterruptedException {

        //given
        int THREAD_CNT = 10;
        News news = News.builder()
                .title("title")
                .content("content")
                .views(0)
                .build();

        newsService.save(news);

        //멀티 쓰레드 생성
        ExecutorService service = Executors.newFixedThreadPool(THREAD_CNT);
        CountDownLatch latch = new CountDownLatch(THREAD_CNT);


        //when
        for (int i = 0; i < THREAD_CNT; i++) {
            service.execute(() -> {
                newsService.updateViews(news);
                latch.countDown();
            });
        }

        latch.await();


        News find = newsService.findById(news.getId());

        //then
        assertEquals(THREAD_CNT, find.getViews());
    }
}
