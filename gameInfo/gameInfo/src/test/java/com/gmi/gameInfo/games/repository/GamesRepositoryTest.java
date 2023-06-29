package com.gmi.gameInfo.games.repository;

import com.gmi.gameInfo.config.TestConfig;
import com.gmi.gameInfo.games.domain.Games;
import com.gmi.gameInfo.games.domain.GamesGenre;
import com.gmi.gameInfo.games.domain.GamesPlatform;
import com.gmi.gameInfo.games.domain.dto.GamesCreateDto;
import com.gmi.gameInfo.games.domain.dto.GamesFindDto;
import com.gmi.gameInfo.genre.domain.Genre;
import com.gmi.gameInfo.genre.repository.GenreRepository;
import com.gmi.gameInfo.platform.domain.Platform;
import com.gmi.gameInfo.platform.repository.PlatformRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({TestConfig.class})
public class GamesRepositoryTest {

    @Autowired
    private GamesRepository gamesRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private GamesGenreRepository gamesGenreRepository;

    @Autowired
    private GamesPlatformRepository gamesPlatformRepository;

    @Autowired
    private PlatformRepository platformRepository;


    @Test
    @Rollback
    @DisplayName("게임 - 저장")
    void save() {

        //given
        GamesCreateDto dto = GamesCreateDto.builder()
                .name("name")
                .explanation("game")
                .mainImage("image")
                .build();

        Games games = Games.createGames(dto);

        //when
        Games save = gamesRepository.save(games);

        //then
        assertEquals("name", save.getName());
    }
    
    @Test
    @Rollback
    @DisplayName("게임 - 단일 조회")
    void findById() {
    
        //given
        Games games = createGameSample("name");

        //when
        Optional<Games> find = gamesRepository.findById(games.getId());

        //then
        assertNotNull(find.orElse(null));
    }
    
    
    @Test
    @DisplayName("게임 - 삭제")
    void delete() {
    
        //given
        Games games = createGameSample("name");
    
        //when
        gamesRepository.delete(games);

        //then
        assertThrows(ChangeSetPersister.NotFoundException.class, () -> {
            gamesRepository.findById(games.getId()).orElseThrow(ChangeSetPersister.NotFoundException::new);
        });
    }
    
    @Test
    @Rollback
    @DisplayName("게임 - 페이지 리스트 조회")
    void findByPageable() {
    
        //given
        Pageable pageable = PageRequest.of(0, 10);

        Games games = createGameSample("pageGames");
        Platform platform = Platform.builder()
                .name("platform1")
                .build();
        Genre genre = Genre.builder()
                .name("genre1")
                .build();
        gamesRepository.save(games);
        platformRepository.save(platform);
        genreRepository.save(genre);

        GamesGenre gamesGenre = new GamesGenre(games, genre);
        GamesPlatform gamesPlatform = new GamesPlatform(games, platform);
        games.associatePlatform(gamesPlatform);
        games.associateGenre(gamesGenre);


        //when

        GamesFindDto dto = GamesFindDto.builder()
                .build();

        List<Games> find = gamesRepository.findByPageable(dto, pageable);
        Set<GamesPlatform> set = find.get(0).getPlatforms();
        Iterator<GamesPlatform> iterator = set.iterator();

        Iterator<GamesGenre> itGenre = find.get(0).getGenres().iterator();

        //then
        assertEquals(1, find.size());
        assertEquals("pageGames" , find.get(0).getName());
        assertEquals("platform1", iterator.next().getPlatform().getName());
        assertEquals("genre1", itGenre.next().getGenre().getName());

    }


    private Games createGameSample(String name) {
        GamesCreateDto dto = GamesCreateDto.builder()
                .name(name)
                .explanation("game")
                .mainImage("image")
                .build();

        Games games = Games.createGames(dto);
        return gamesRepository.save(games);
    }

}
