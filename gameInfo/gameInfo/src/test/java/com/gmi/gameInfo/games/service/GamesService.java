package com.gmi.gameInfo.games.service;

import com.gmi.gameInfo.games.domain.Games;
import com.gmi.gameInfo.games.domain.GamesGenre;
import com.gmi.gameInfo.games.domain.GamesPlatform;
import com.gmi.gameInfo.games.domain.dto.GamesFindDto;
import com.gmi.gameInfo.games.exception.NotFoundGameException;
import com.gmi.gameInfo.games.repository.GamesGenreRepository;
import com.gmi.gameInfo.games.repository.GamesPlatformRepository;
import com.gmi.gameInfo.games.repository.GamesRepository;
import com.gmi.gameInfo.genre.domain.Genre;
import com.gmi.gameInfo.platform.domain.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Pageable;
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

        for (Platform platform : platforms) {
            GamesPlatform gamesPlatform = new GamesPlatform(games, platform);
            games.associatePlatform(gamesPlatform);
        }

        for (Genre genre : genres) {
            GamesGenre gamesGenre = new GamesGenre(games, genre);
            games.associateGenre(gamesGenre);
        }

        return save;
    }

    @Transactional
    public void delete(Games games) {
        games.updateDelete();

    }

    public Games findById(Long id) {
        return gamesRepository.findById(id).orElseThrow(NotFoundGameException::new);
    }


    @Transactional
    public List<Games> findByPageable(GamesFindDto dto, Pageable pageable) {
        return gamesRepository.findByPageable(dto, pageable);
    }

    @Transactional
    public Games findDetailById(Long id) {
        return gamesRepository.findOneDetailById(id).orElseThrow(NotFoundGameException::new);
    }

}
