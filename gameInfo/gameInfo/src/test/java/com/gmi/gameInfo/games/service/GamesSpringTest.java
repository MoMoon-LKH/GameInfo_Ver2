package com.gmi.gameInfo.games.service;

import com.gmi.gameInfo.games.domain.Games;
import com.gmi.gameInfo.games.domain.dto.GamesCreateDto;
import com.gmi.gameInfo.games.exception.NotFoundGameException;
import com.gmi.gameInfo.games.repository.GamesRepository;
import com.gmi.gameInfo.genre.domain.Genre;
import com.gmi.gameInfo.genre.service.GenreService;
import com.gmi.gameInfo.platform.domain.Platform;
import com.gmi.gameInfo.platform.service.PlatformService;
import net.bytebuddy.description.method.ParameterList;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GamesSpringTest {

    @Autowired
    GamesService gamesService;

    @Autowired
    PlatformService platformService;

    @Autowired
    GenreService genreService;

    @Autowired
    GamesRepository gamesRepository;

    private Games games;

    private List<Platform> platforms = new ArrayList<>();

    private List<Genre> genres = new ArrayList<>();

    @BeforeAll
    void setUp() {

        platforms.add(platformService.save(Platform.builder().name("plat1").build()));
        platforms.add(platformService.save(Platform.builder().name("plat2").build()));


        genres.add(genreService.save(Genre.builder().name("genre1").build()));
        genres.add(genreService.save(Genre.builder().name("genre2").build()));

        games = Games.createGames(GamesCreateDto.builder()
                .name("game")
                .explanation("ex")
                .mainImage("main")
                .releaseDate(LocalDate.now())
                .build());

        gamesService.save(games, platforms, genres);
    }

    @Test
    @DisplayName("게임 업데이트 - 낙관적 락 수정시 version 1")
    void update_games() {

        //given
        GamesCreateDto createDto = GamesCreateDto.builder()
                .name("update")
                .explanation("ex")
                .mainImage("main")
                .releaseDate(LocalDate.now())
                .build();


        //when
        Games update = gamesService.update(games.getId(), createDto, platforms, genres);
        Games find = gamesService.findDetailById(update.getId());

        //then
        assertEquals("update", find.getName());
        assertEquals(1, find.getVersion());
    }

    
    @Test
    @DisplayName("게임 업데이트 - 낙관적 락")
    void updateGames_() throws InterruptedException {
    
        //given
        int THREAD_CNT = 4;
        GamesCreateDto dto = GamesCreateDto.builder()
                .name("updateName")
                .explanation("ex")
                .mainImage("main")
                .releaseDate(LocalDate.now())
                .version(0)
                .build();

        ExecutorService service = Executors.newFixedThreadPool(THREAD_CNT);
        CountDownLatch latch = new CountDownLatch(THREAD_CNT);
        List<Genre> genreList = new ArrayList<>();
        List<Platform> platformList = new ArrayList<>();


        //when
        for (int i = 0; i < THREAD_CNT; i++) {
            try {
                service.execute(() -> {
                    gamesService.update(games.getId(), dto, platformList, genreList);
                });

            } finally {
                latch.countDown();
            }
        }
        latch.await();

        Games find = gamesService.findDetailById(games.getId());

        //then
        assertEquals(dto.getName(), find.getName());
        assertEquals(1, find.getVersion());
    }

}
