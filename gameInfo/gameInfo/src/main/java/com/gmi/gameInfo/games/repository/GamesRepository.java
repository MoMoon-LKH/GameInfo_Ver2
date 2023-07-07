package com.gmi.gameInfo.games.repository;

import com.gmi.gameInfo.games.domain.Games;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GamesRepository extends JpaRepository<Games, Long>, GamesCustomRepository {

    int countByIdAndDeleteYn(@Param("id") Long id, @Param("deleteYn") boolean deleteYn);
}
