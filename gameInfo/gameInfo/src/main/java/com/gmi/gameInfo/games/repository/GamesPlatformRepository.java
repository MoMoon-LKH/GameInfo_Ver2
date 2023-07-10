package com.gmi.gameInfo.games.repository;

import com.gmi.gameInfo.games.domain.GamesPlatform;
import com.gmi.gameInfo.games.domain.GamesPlatformId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GamesPlatformRepository extends JpaRepository<GamesPlatform, GamesPlatformId> {

    List<GamesPlatform> findAllByGamesId(Long gamesId);
}
