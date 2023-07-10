package com.gmi.gameInfo.games.repository;

import com.gmi.gameInfo.games.domain.Games;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface GamesRepository extends JpaRepository<Games, Long>, GamesCustomRepository {

    int countByIdAndDeleteYn(@Param("id") Long id, @Param("deleteYn") boolean deleteYn);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select g from Games g where g.id = :id")
    Optional<Games> findByIdOptimisticLock(@Param("id") Long id);
}
