package com.gmi.gameInfo.news.repository;


import com.gmi.gameInfo.news.domain.News;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>, NewsCustomRepository {

    int countByPlatformId(@Param("platformId") Long platformId);
}
