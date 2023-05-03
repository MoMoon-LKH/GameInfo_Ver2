package com.gmi.gameInfo.likes.service;

import com.gmi.gameInfo.likes.domain.LikeType;
import com.gmi.gameInfo.likes.domain.NewsLikes;
import com.gmi.gameInfo.likes.domain.dto.LikeDto;
import com.gmi.gameInfo.likes.repository.NewsLikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NewsLikesService {

    private final NewsLikesRepository likesRepository;

    @Transactional
    public NewsLikes save(NewsLikes likes) {
        return likesRepository.save(likes);
    }

    @Transactional
    public void updateType(NewsLikes likes, LikeType type) {
        likes.updateLikes(type);
    }

    @Transactional
    public void delete(NewsLikes likes) {
        likesRepository.delete(likes);
    }


    public Optional<NewsLikes> findById(Long id) {
        return likesRepository.findById(id);
    }

    public Optional<NewsLikes> findByNewsIdAndMemberId(Long newsId, Long memberId) {
        return likesRepository.findByNewsIdAndMemberId(newsId, memberId);
    }

    public int countByNewsIdAndLikesType(Long newsId, LikeType likeType) {
        return likesRepository.countByNewsIdAndLikeType(newsId, likeType);
    }

    public LikeDto findLikeDtoByType(Long newsId, Long memberId, LikeType likeType) {
        return LikeDto.builder()
                .count(likesRepository.countByNewsIdAndLikeType(newsId, likeType))
                .checkedStatus(likesRepository.existsByNewsIdAndMemberIdAndLikeType(newsId, memberId, likeType))
                .build();
    }
}
