package com.gmi.gameInfo.news.repository;


import com.gmi.gameInfo.news.domain.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>, NewsCustomRepository {

    int countByPlatformId(@Param("platformId") Long platformId);

    int countAllBy();


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "1000")})
    @Query("select n from News n where n.id = :id")
    Optional<News> findByIdForViews(@Param("id") Long id);
}
