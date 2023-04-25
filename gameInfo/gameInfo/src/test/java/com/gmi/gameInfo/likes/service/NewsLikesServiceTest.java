package com.gmi.gameInfo.likes.service;


import com.gmi.gameInfo.likes.domain.LikesType;
import com.gmi.gameInfo.likes.domain.NewsLikes;
import com.gmi.gameInfo.likes.repository.NewsLikesRepository;
import com.gmi.gameInfo.member.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
@Rollback
public class NewsLikesServiceTest {

    @Mock
    private NewsLikesRepository likesRepository;

    @InjectMocks
    private NewsLikesService newsLikesService;
    
    @Test
    @DisplayName("저장 테스트")
    void saveNewsLike() {
    
        //given
        NewsLikes likes = NewsLikes.builder()
                .likesType(LikesType.LIKE)
                .build();
        NewsLikes saveLike = NewsLikes.builder()
                .id(1L)
                .likesType(LikesType.LIKE)
                .build();

        given(likesRepository.save(likes)).willReturn(saveLike);

        //when
        NewsLikes save = newsLikesService.save(likes);

        //then
        assertEquals(1L, save.getId());
    }
    
    @Test
    @DisplayName("업데이트 테스트")
    void updateNewsLike() {
    
        //given
        NewsLikes likes = NewsLikes.builder()
                .likesType(LikesType.LIKE)
                .build();
    
        //when
        newsLikesService.updateType(likes, LikesType.DISLIKE);
        
        //then
        assertEquals(LikesType.DISLIKE, likes.getLikesType());

    }

    @Test
    @DisplayName("삭제 테스트")
    void deleteNewsLike() {

        //given
        NewsLikes likes = NewsLikes.builder()
                .id(1L)
                .likesType(LikesType.LIKE)
                .build();
        doNothing().when(likesRepository).delete(any());
        given(likesRepository.findById(any())).willReturn(Optional.ofNullable(null));

        //when
        newsLikesService.delete(likes);
        Optional<NewsLikes> find = newsLikesService.findById(likes.getId());

        boolean bool = true;

        if (!find.isPresent()) {
            bool = false;
        }

        //then
        assertFalse(bool);
    }

    @Test
    @DisplayName("NewsLikes 단일조회")
    void findById() {
    
        //given
        NewsLikes likes = NewsLikes.builder()
                .id(1L)
                .likesType(LikesType.LIKE)
                .build();
        given(likesRepository.findById(any())).willReturn(Optional.of(likes));

        //when
        Optional<NewsLikes> find = newsLikesService.findById(1L);


        //then
        assertSame(likes, find.get());
    }

    @Test
    @DisplayName("NewsLikes 단일 조회 - newsId, memberId")
    void findByNewsIdAndMemberId() {

        //given
        NewsLikes likes = NewsLikes.builder()
                .id(1L)
                .likesType(LikesType.LIKE)
                .build();
        given(likesRepository.findByNewsIdAndMemberId(any(Long.class), any(Long.class))).willReturn(Optional.of(likes));

        //when
        Optional<NewsLikes> find = newsLikesService.findByNewsIdAndMemberId(1L, 1L);

        //then
        assertSame(find.get(), likes);
    }
}
