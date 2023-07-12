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
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({TestConfig.class})
@Transactional
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
    @DisplayName("게임 - 페이지 리스트 조회")
    @Transactional
    void findByPageable() {
    
        //given
        Pageable pageable = PageRequest.of(0, 10);
        List<Platform> platforms = new ArrayList<>();
        Platform platform1 = Platform.builder().name("platform1").build();
        platforms.add(platformRepository.save(platform1));

        List<Genre> genres = new ArrayList<>();
        Genre genre1 = Genre.builder().name("genre1").build();
        genres.add(genreRepository.save(genre1));

        Games games = createGame("pageGames", platforms, genres);

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

    @Test
    @DisplayName("게임 - 페이지 리스트 조회 - 검색 조건 name")
    void findByPageable_name() {

        //given
        Pageable pageable = PageRequest.of(0, 10);
        List<Platform> platforms = new ArrayList<>();
        Platform platform1 = Platform.builder().name("platform1").build();
        Platform platform2 = Platform.builder().name("platform2").build();
        platforms.add(platformRepository.save(platform1));

        List<Genre> genres = new ArrayList<>();
        Genre genre1 = Genre.builder().name("genre1").build();
        genres.add(genreRepository.save(genre1));

        Games games1 = createGame("games1", platforms, genres);

        platforms.add(platformRepository.save(platform2));

        Games games2 = createGame("games2", platforms, genres);

        //when
        GamesFindDto gamesFindDto = GamesFindDto.builder()
                .search("games")
                .build();

        List<Games> find = gamesRepository.findByPageable(gamesFindDto, pageable);

        //then
        assertEquals(2, find.size());
    }


    @Test
    @DisplayName("게임 - 페이지 리스트 조회 - 검색 조건 platformIds")
    void findByPageable_platformIds() {

        //given
        Pageable pageable = PageRequest.of(0, 10);
        List<Platform> platforms = new ArrayList<>();
        Platform platform1 = Platform.builder().name("platform1").build();
        Platform platform2 = Platform.builder().name("platform2").build();
        platforms.add(platformRepository.save(platform1));

        List<Genre> genres = new ArrayList<>();
        Genre genre1 = Genre.builder().name("genre1").build();
        genres.add(genreRepository.save(genre1));

        Games games1 = createGame("games1", platforms, genres);

        platforms.add(platformRepository.save(platform2));

        Games games2 = createGame("games2", platforms, genres);


        //when
        List<Long> platformsIds = new ArrayList<>();
        platformsIds.add(platform2.getId());

        GamesFindDto gamesFindDto = GamesFindDto.builder()
                .platformIds(platformsIds)
                .build();

        List<Games> find = gamesRepository.findByPageable(gamesFindDto, pageable);

        //then
        assertEquals(1, find.size());
    }


    @Test
    @DisplayName("게임 - 페이지 리스트 조회 - 검색 조건 genreIds")
    void findByPageable_genreIds() {

        //given
        Pageable pageable = PageRequest.of(0, 10);
        List<Platform> platforms = new ArrayList<>();
        Platform platform1 = Platform.builder().name("platform1").build();
        platforms.add(platformRepository.save(platform1));

        List<Genre> genres = new ArrayList<>();
        Genre genre1 = Genre.builder().name("genre1").build();
        Genre genre2 = Genre.builder().name("genre2").build();
        genres.add(genreRepository.save(genre1));

        Games games1 = createGame("games1", platforms, genres);

        genres.add(genreRepository.save(genre2));

        Games games2 = createGame("games2", platforms, genres);

        
        //when
        List<Long> genreIds = new ArrayList<>();
        genreIds.add(genre2.getId());

        GamesFindDto gamesFindDto = GamesFindDto.builder()
                .genreIds(genreIds)
                .build();

        List<Games> find = gamesRepository.findByPageable(gamesFindDto, pageable);

        //then
        assertEquals(1, find.size());
    }

    @Test
    @DisplayName("")
    void findByPageable_genreIdsAndPlatformIds() {

        Pageable pageable = PageRequest.of(0, 10);
        List<Platform> platforms = new ArrayList<>();
        Platform platform1 = Platform.builder().name("platform1").build();
        Platform platform2 = Platform.builder().name("platform2").build();
        platforms.add(platformRepository.save(platform1));


        List<Genre> genres = new ArrayList<>();
        Genre genre1 = Genre.builder().name("genre1").build();
        Genre genre2 = Genre.builder().name("genre2").build();
        genres.add(genreRepository.save(genre1));

        Games games1 = createGame("games1", platforms, genres);

        genres.add(genreRepository.save(genre2));
        platforms.add(platformRepository.save(platform2));

        Games games2 = createGame("games2", platforms, genres);


        //when
        List<Long> genreIds = new ArrayList<>();
        genreIds.add(genre2.getId());
        genreIds.add(genre1.getId());

        List<Long> platformIds = new ArrayList<>();
        platformIds.add(platform2.getId());

        GamesFindDto gamesFindDto = GamesFindDto.builder()
                .genreIds(genreIds)
                .platformIds(platformIds)
                .build();

        List<Games> find = gamesRepository.findByPageable(gamesFindDto, pageable);

        //then
        assertEquals(1, find.size());
    }

    @Test
    @DisplayName("게임 - 단일 조회 (fetch Join)")
    @Transactional
    void findOneById() {
    
        //given
        List<Platform> platforms = new ArrayList<>();
        Platform platform1 = Platform.builder().name("platform1").build();
        Platform platform2 = Platform.builder().name("platform2").build();
        platforms.add(platformRepository.save(platform1));
        platforms.add(platformRepository.save(platform2));

        List<Genre> genres = new ArrayList<>();
        Genre genre1 = Genre.builder().name("genre1").build();
        genres.add(genreRepository.save(genre1));

        Games games = createGame("Games", platforms, genres);


        //when
        Games find = gamesRepository.findOneDetailById(games.getId()).orElse(null);
        Iterator<GamesPlatform> gamesPlatforms = find.getPlatforms().iterator();

        //then
        assertNotNull(find);
        assertEquals(find.getName(), "Games");
        assertEquals(find.getPlatforms().size(), 2);
        assertEquals(find.getGenres().size(), 1);
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


    private Games createGame(String gameName, List<Platform> platforms, List<Genre> genres) {
        Games games = createGameSample(gameName);

        gamesRepository.save(games);

        Set<GamesPlatform> gamesPlatforms = new HashSet<>();

        for (Platform platform : platforms) {
            GamesPlatform gamesPlatform = new GamesPlatform(games, platform);
            gamesPlatforms.add(gamesPlatform);
            gamesPlatformRepository.save(gamesPlatform);
        }
        games.setPlatforms(gamesPlatforms);


        Set<GamesGenre> gamesGenres = new HashSet<>();

        for (Genre genre : genres) {
            GamesGenre gamesGenre = new GamesGenre(games, genre);
            gamesGenres.add(gamesGenre);
            gamesGenreRepository.save(gamesGenre);
        }
        games.setGenres(gamesGenres);

        return games;
    }

}
