package com.gmi.gameInfo.games.service;

import com.gmi.gameInfo.games.domain.Games;
import com.gmi.gameInfo.games.domain.GamesGenre;
import com.gmi.gameInfo.games.domain.GamesPlatform;
import com.gmi.gameInfo.games.exception.NotFoundGameException;
import com.gmi.gameInfo.games.repository.GamesGenreRepository;
import com.gmi.gameInfo.games.repository.GamesPlatformRepository;
import com.gmi.gameInfo.games.repository.GamesRepository;
import com.gmi.gameInfo.genre.domain.Genre;
import com.gmi.gameInfo.platform.domain.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GamesService {

    private final GamesRepository gamesRepository;

    private final GamesPlatformRepository gamesPlatformRepository;

    private final GamesGenreRepository gamesGenreRepository;


    @Transactional
    public Games save(Games games, List<Platform> platforms, List<Genre> genres) {
        Games save = gamesRepository.save(games);

        List<GamesPlatform> gamesPlatforms = new ArrayList<>();
        List<GamesGenre> gamesGenres = new ArrayList<>();

        for (Platform platform : platforms) {
            gamesPlatforms.add(new GamesPlatform(games, platform));
        }

        for (Genre genre : genres) {
            gamesGenres.add(new GamesGenre(games, genre));
        }

        gamesPlatformRepository.saveAll(gamesPlatforms);
        gamesGenreRepository.saveAll(gamesGenres);

        return save;
    }

    @Transactional
    public void delete(Games games) {
        games.updateDelete();

    }

    public Games findById(Long id) {
        return gamesRepository.findById(id).orElseThrow(NotFoundGameException::new);
    }




}
