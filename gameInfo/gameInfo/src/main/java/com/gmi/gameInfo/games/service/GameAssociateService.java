package com.gmi.gameInfo.games.service;

import com.gmi.gameInfo.games.domain.Games;
import com.gmi.gameInfo.games.domain.GamesGenre;
import com.gmi.gameInfo.games.domain.GamesPlatform;
import com.gmi.gameInfo.games.repository.GamesGenreRepository;
import com.gmi.gameInfo.games.repository.GamesPlatformRepository;
import com.gmi.gameInfo.genre.domain.Genre;
import com.gmi.gameInfo.platform.domain.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameAssociateService {

    private final GamesPlatformRepository gamesPlatformRepository;

    private final GamesGenreRepository gamesGenreRepository;


    @Transactional
    public void addGamesPlatforms(Games games, List<Platform> platforms) {
        for (Platform platform : platforms) {
            GamesPlatform gamesPlatform = new GamesPlatform(games, platform);
            gamesPlatformRepository.save(gamesPlatform);
            games.associatePlatform(gamesPlatform);
        }
    }

    @Transactional
    public void addGamesGenre(Games games, List<Genre> genres) {
        for (Genre genre : genres) {
            GamesGenre gamesGenre = new GamesGenre(games, genre);
            gamesGenreRepository.save(gamesGenre);
            games.associateGenre(gamesGenre);
        }
    }


}
