package com.gmi.gameInfo.games.repository;

import com.gmi.gameInfo.games.domain.GamesGenre;
import com.gmi.gameInfo.games.domain.GamesGenreId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GamesGenreRepository extends JpaRepository<GamesGenre, GamesGenreId> {
}
