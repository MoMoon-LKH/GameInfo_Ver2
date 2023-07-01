package com.gmi.gameInfo.games.repository;

import com.gmi.gameInfo.games.domain.Games;
import com.gmi.gameInfo.games.domain.dto.GamesFindDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface GamesCustomRepository {

    List<Games> findByPageable(GamesFindDto dto, Pageable pageable);

    Optional<Games> findOneDetailById(Long id);
}
