package com.gmi.gameInfo.likes.service;


import com.gmi.gameInfo.likes.domain.LikeType;
import com.gmi.gameInfo.likes.domain.NewsLikes;
import com.gmi.gameInfo.likes.domain.dto.LikeDto;
import com.gmi.gameInfo.likes.repository.NewsLikesRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Rollback;

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
                .likeType(LikeType.LIKE)
                .build();
        NewsLikes saveLike = NewsLikes.builder()
                .id(1L)
                .likeType(LikeType.LIKE)
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
                .likeType(LikeType.LIKE)
                .build();
    
        //when
        newsLikesService.updateType(likes, LikeType.DISLIKE);
        
        //then
        assertEquals(LikeType.DISLIKE, likes.getLikeType());

    }

    @Test
    @DisplayName("삭제 테스트")
    void deleteNewsLike() {

        //given
        NewsLikes likes = NewsLikes.builder()
                .id(1L)
                .likeType(LikeType.LIKE)
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
                .likeType(LikeType.LIKE)
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
                .likeType(LikeType.LIKE)
                .build();
        given(likesRepository.findByNewsIdAndMemberId(any(Long.class), any(Long.class))).willReturn(Optional.of(likes));

        //when
        Optional<NewsLikes> find = newsLikesService.findByNewsIdAndMemberId(1L, 1L);

        //then
        assertSame(find.get(), likes);
    }
    
    @Test
    @DisplayName("NewsLikes LikeType 따른 총 수 조회")
    void countByNewsIdAndMemberId() {
    
        //given
        given(likesRepository.countByNewsIdAndLikeType(any(Long.class), any(LikeType.class))).willReturn(2);

        //when
        int count = newsLikesService.countByNewsIdAndLikesType(1L, LikeType.LIKE);
        
        //then
        assertEquals(2, count);
    }

    @Test
    @DisplayName("LikeDto 조회")
    void findLikeDtoByType() {

        //given
        given(likesRepository.countByNewsIdAndLikeType(any(Long.class), any(LikeType.class))).willReturn(1);
        given(likesRepository.existsByNewsIdAndMemberIdAndLikeType(any(Long.class), any(Long.class), any(LikeType.class))).willReturn(true);

        //when
        LikeDto likeDto = newsLikesService.findLikeDtoByType(1L, 1L, LikeType.LIKE);

        //then
        assertEquals(1, likeDto.getCount());

    }
}
