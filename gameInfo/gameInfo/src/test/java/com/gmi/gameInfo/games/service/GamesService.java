package com.gmi.gameInfo.games.service;

import com.gmi.gameInfo.games.domain.Games;
import com.gmi.gameInfo.games.repository.GamesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GamesService {

    private final GamesRepository gamesRepository;


    @Transactional
    public Games save(Games games) {
        return gamesRepository.save(games);
    }


}
