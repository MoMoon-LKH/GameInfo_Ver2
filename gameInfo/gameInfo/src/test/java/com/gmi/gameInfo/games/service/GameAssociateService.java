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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GameAssociateService {

    private final GamesPlatformRepository gamesPlatformRepository;

    private final GamesGenreRepository gamesGenreRepository;


    @Transactional
    public void addGamesPlatforms(Games games, List<Platform> platforms) {
        Set<GamesPlatform> newPlatforms = new HashSet<>();

        for (Platform platform : platforms) {
            GamesPlatform gamesPlatform = new GamesPlatform(games, platform);
            gamesPlatformRepository.save(gamesPlatform);
            newPlatforms.add(gamesPlatform);
        }

        games.setPlatforms(newPlatforms);
    }

    @Transactional
    public void addGamesGenre(Games games, List<Genre> genres) {
        Set<GamesGenre> newGenres = new HashSet<>();

        for (Genre genre : genres) {
            GamesGenre gamesGenre = new GamesGenre(games, genre);
            gamesGenreRepository.save(gamesGenre);
            newGenres.add(gamesGenre);
        }

        games.setGenres(newGenres);
    }


}
