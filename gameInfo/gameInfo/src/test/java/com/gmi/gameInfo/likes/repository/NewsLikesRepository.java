package com.gmi.gameInfo.likes.repository;

import com.gmi.gameInfo.likes.domain.NewsLikes;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsLikesRepository extends JpaRepository<NewsLikes, Long> {

    Optional<NewsLikes> findByNewsIdAndMemberId(@Param("newsId") Long newsId, @Param("memberId") Long memberId);
}
