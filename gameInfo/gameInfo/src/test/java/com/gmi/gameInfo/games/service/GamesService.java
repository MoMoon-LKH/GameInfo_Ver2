package com.gmi.gameInfo.games.service;

import com.gmi.gameInfo.games.domain.Games;
import com.gmi.gameInfo.games.domain.GamesGenre;
import com.gmi.gameInfo.games.domain.GamesPlatform;
import com.gmi.gameInfo.games.domain.dto.GamesCreateDto;
import com.gmi.gameInfo.games.domain.dto.GamesFindDto;
import com.gmi.gameInfo.games.exception.NotFoundGameException;
import com.gmi.gameInfo.games.exception.GameSameNameException;
import com.gmi.gameInfo.games.repository.GamesGenreRepository;
import com.gmi.gameInfo.games.repository.GamesPlatformRepository;
import com.gmi.gameInfo.games.repository.GamesRepository;
import com.gmi.gameInfo.genre.domain.Genre;
import com.gmi.gameInfo.platform.domain.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GamesService {

    private final GamesRepository gamesRepository;

    private final GamesPlatformRepository gamesPlatformRepository;

    private final GamesGenreRepository gamesGenreRepository;

    private final GameAssociateService gameAssociateService;


    @Transactional
    public Games save(Games games, List<Platform> platforms, List<Genre> genres) {
        try {

            gamesRepository.save(games);
            gameAssociateService.addGamesGenre(games, genres);
            gameAssociateService.addGamesPlatforms(games, platforms);

            return games;

        } catch (DataIntegrityViolationException e) {
            throw new GameSameNameException();
        }
    }

    @Transactional
    public Games update(Long id, GamesCreateDto dto, List<Platform> platforms, List<Genre> genres) {

        Games games = gamesRepository.findByIdOptimisticLock(id).orElseThrow(NotFoundGameException::new);

        Set<GamesPlatform> delPlatforms = games.getPlatforms();
        Set<GamesGenre> delGenres = games.getGenres();

        gamesGenreRepository.deleteAll(delGenres);
        gamesPlatformRepository.deleteAll(delPlatforms);

        games.update(dto);

        gameAssociateService.addGamesPlatforms(games, platforms);
        gameAssociateService.addGamesGenre(games, genres);


        return games;
    }

    @Transactional
    public void delete(Games games) {
        games.updateDeleteY();
    }

    public int countByIdAndDeleteN(Long id) {
        return gamesRepository.countByIdAndDeleteYn(id, false);
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
