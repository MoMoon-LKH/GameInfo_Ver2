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
    @Transactional
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
    @Transactional
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
    @Transactional
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
        List<String> platforms = new ArrayList<>();
        platforms.add("platform1");

        List<String> genres = new ArrayList<>();
        genres.add("genre1");

        Map<String, Object> map = createGameAndPlatformAndGenre("pageGames", platforms, genres);

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
    @DisplayName("게임 - 단일 조회 (fetch Join)")
    @Transactional
    void findOneById() {
    
        //given
        List<String> platformName = new ArrayList<>();
        platformName.add("platform1");
        platformName.add("platform2");

        Platform platform = Platform.builder()
                .name("dummy1")
                .build();
        platformRepository.save(platform);

        List<String> genreName = new ArrayList<>();
        genreName.add("genre1");

        Map<String, Object> map = createGameAndPlatformAndGenre("Games", platformName, genreName);

        Games games = (Games) map.get("game");

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


    private Map<String, Object> createGameAndPlatformAndGenre(String gameName, List<String> pName, List<String> gName) {
        Games games = createGameSample(gameName);
        List<Platform> platforms = new ArrayList<>();
        List<Genre> genres = new ArrayList<>();

        for (String name : pName) {
            Platform platform = Platform.builder()
                    .name(name)
                    .build();
            platformRepository.save(platform);
            platforms.add(platform);
        }


        for (String name : gName) {
            Genre genre = Genre.builder()
                    .name(name)
                    .build();
            genreRepository.save(genre);
            genres.add(genre);
        }
        gamesRepository.save(games);

        Set<GamesPlatform> gamesPlatforms = new HashSet<>();

        for (Platform platform : platforms) {
            GamesPlatform gamesPlatform = new GamesPlatform(games, platform);
            gamesPlatforms.add(gamesPlatform);
        }
        games.setPlatforms(gamesPlatforms);


        Set<GamesGenre> gamesGenres = new HashSet<>();

        for (Genre genre : genres) {
            GamesGenre gamesGenre = new GamesGenre(games, genre);
            gamesGenres.add(gamesGenre);
        }
        games.setGenres(gamesGenres);

        Map<String, Object> map = new HashMap<>();
        map.put("game", games);
        map.put("genre", genres);
        map.put("platform", platforms);

        return map;
    }

}
