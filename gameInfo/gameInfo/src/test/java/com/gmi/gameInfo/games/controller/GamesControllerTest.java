package com.gmi.gameInfo.games.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmi.gameInfo.config.TestConfig;
import com.gmi.gameInfo.games.domain.Games;
import com.gmi.gameInfo.games.domain.GamesGenre;
import com.gmi.gameInfo.games.domain.GamesPlatform;
import com.gmi.gameInfo.games.domain.dto.GamesCreateDto;
import com.gmi.gameInfo.games.service.GamesService;
import com.gmi.gameInfo.genre.domain.Genre;
import com.gmi.gameInfo.genre.service.GenreService;
import com.gmi.gameInfo.platform.domain.Platform;
import com.gmi.gameInfo.platform.service.PlatformService;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(GamesController.class)
@ActiveProfiles("dev")
public class GamesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GamesService gamesService;

    @MockBean
    private PlatformService platformService;

    @MockBean
    private GenreService genreService;

    @Autowired
    ObjectMapper objectMapper;

    private Platform platform1 = Platform.builder()
            .id(1L)
            .name("platform1")
            .build();

    private Genre genre = Genre.builder()
            .id(1L)
            .name("genre1")
            .build();


    @Test
    @WithMockUser
    @DisplayName("Games - 저장 API")
    void saveGames() throws Exception{

        //given
        List<Long> genreIds = new ArrayList<>();
        genreIds.add(1L);

        List<Long> platformIds = new ArrayList<>();
        platformIds.add(1L);

        GamesCreateDto gamesCreateDto = GamesCreateDto.builder()
                .name("game")
                .explanation("explanation")
                .genreList(genreIds)
                .mainImage("/games/game")
                .platformList(platformIds)
                .build();

        List<Genre> genres = new ArrayList<>();
        genres.add(Genre.builder().id(1L).name("genre1").build());

        List<Platform> platforms = new ArrayList<>();
        platforms.add(Platform.builder().id(1L).name("platform1").build());

        Games sam = Games.createGames(gamesCreateDto);

        Games game = new Games(
                1L,
                sam.getName(),
                sam.getExplanation(),
                sam.getMainImage(),
                LocalDate.now(),
                false,
                LocalDate.now(),
                getGamesGenre(sam, genres),
                getGamesPlatform(sam, platforms),
                0
        );

        given(genreService.findAllByIdsIn(any())).willReturn(genres);
        given(platformService.findAllByIdsIn(any())).willReturn(platforms);
        given(gamesService.save(any(), any(), any())).willReturn(game);
        given(gamesService.findDetailById(any())).willReturn(game);

        //when
        ResultActions perform = mockMvc.perform(
                post("/api/game")
                        .content(objectMapper.writeValueAsString(gamesCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        //then
        perform
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(gamesCreateDto.getName()))
                .andExpect(jsonPath("$.genreList.size()").value(1))
                .andExpect(jsonPath("$.platformList.size()").value(1));
    }


    @Test
    @WithMockUser
    @DisplayName("게임 - 리스트 조회 (페이징), Genre 및 Platform 둘 다 null 값인 경우")
    void findAllByPaging_NotFindPlatformAndGenre() throws Exception {

        //given
        Pageable page = PageRequest.of(0, 1);

        GamesCreateDto gamesCreateDto = GamesCreateDto.builder()
                .name("game")
                .explanation("explanation")
                .mainImage("/games/game")
                .releaseDate(LocalDate.now())
                .build();
        GamesCreateDto gamesCreateDto2 = GamesCreateDto.builder()
                .name("game2")
                .explanation("explanation2")
                .mainImage("/games/game")
                .releaseDate(LocalDate.now())
                .build();


        List<Games> gamesList = new ArrayList<>();
        //gamesList.add(createGamesAssociatePlatformsAndGenres(1L, Games.createGames(gamesCreateDto), 1, 1));
        gamesList.add(createGamesAssociatePlatformsAndGenres(2L, Games.createGames(gamesCreateDto2), 1, 2));

        given(gamesService.findByPageable(any(), any())).willReturn(gamesList);

        //when
        ResultActions result = mockMvc.perform(get("/api/game/list")
                .param("page", String.valueOf(page.getPageNumber()))
                .param("size", String.valueOf(page.getPageSize()))
                .param("search", "")
                .with(csrf()));

        //then
        result
                .andDo(print())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$.[0].name").value("game2"));
    }


    @Test
    @WithMockUser
    @DisplayName("게임 - 리스트 조회 (페이징)")
    void findAllByPaging() throws Exception{

        //given
        GamesCreateDto gamesCreateDto = GamesCreateDto.builder()
                .name("game")
                .explanation("explanation")
                .mainImage("/games/game")
                .releaseDate(LocalDate.now())
                .build();

        List<Games> gamesList = new ArrayList<>();
        gamesList.add(createGamesAssociatePlatformsAndGenres(1L, Games.createGames(gamesCreateDto), 1, 1));
        Pageable page = PageRequest.of(0, 2);

        given(gamesService.findByPageable(any(), any())).willReturn(gamesList);

        List<Long> genres = new ArrayList<>();
        genres.add(1L);

        List<Long> platforms = new ArrayList<>();
        platforms.add(1L);


        //when
        ResultActions result = mockMvc.perform(get("/api/game/list")
                .param("page", String.valueOf(page.getPageNumber()))
                .param("size", String.valueOf(page.getPageSize()))
                .param("search", "")
                        .param("genre", Arrays.toString(genres.toArray()))
                        .param("platform", Arrays.toString(platforms.toArray()))
                .with(csrf()));

        //then
        result
                .andDo(print())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$.[0].name").value("game"));
    }


    @Test
    @WithMockUser
    @DisplayName("게임 - 삭제 (실패)")
    void deleteGames_FAIL() throws Exception{

        //given
        GamesCreateDto gamesCreateDto = GamesCreateDto.builder()
                .name("game")
                .explanation("explanation")
                .mainImage("/games/game")
                .releaseDate(LocalDate.now())
                .build();
        Games games = Games.createGames(gamesCreateDto);

        given(gamesService.findById(any())).willReturn(games);
        given(gamesService.countByIdAndDeleteN(any())).willReturn(1);

        //when
        ResultActions result = mockMvc.perform(delete("/api/game/1")
                .with(csrf())
        );

        //then
        result
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("게임 - 삭제 (성공)")
    @WithMockUser
    void deleteGames_SUCCESS() throws Exception {

        //given
        GamesCreateDto gamesCreateDto = GamesCreateDto.builder()
                .name("game")
                .explanation("explanation")
                .mainImage("/games/game")
                .releaseDate(LocalDate.now())
                .build();
        Games games = Games.createGames(gamesCreateDto);

        given(gamesService.findById(any())).willReturn(games);
        given(gamesService.countByIdAndDeleteN(any())).willReturn(0);

        //when
        ResultActions result = mockMvc.perform(delete("/api/game/1")
                .with(csrf())
        );


        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }


    @Test
    @WithMockUser
    @DisplayName("게임 업데이트 - 성공")
    void update_games_optimistic() throws Exception {

        //given
        GamesCreateDto updateDto = GamesCreateDto.builder()
                .name("update")
                .explanation("explation")
                .mainImage("/gaems/game")
                .releaseDate(LocalDate.now())
                .build();

        Games games = Games.createGames(updateDto);
        List<Platform> platforms = new ArrayList<>();
        List<Genre> genres = new ArrayList<>();

        given(platformService.findAllByIdsIn(any())).willReturn(platforms);
        given(genreService.findAllByIdsIn(any())).willReturn(genres);
        given(gamesService.update(any(), any(), any(), any())).willReturn(games);

        //when
        ResultActions perform = mockMvc.perform(put("/api/game/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto))
                .with(csrf()));

        //then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updateDto.getName()));

    }


    private Games createGamesAssociatePlatformsAndGenres(Long gamesId , Games games, int pCount, int gCount) {

        List<Platform> platforms = new ArrayList<>();
        List<Genre> genres = new ArrayList<>();

        for (int i = 1; i <= pCount; i++) {
            platforms.add(Platform.builder().id((long) i).name("platform" + i).build());
        }

        for (int i = 1; i <= gCount; i++) {
            genres.add(Genre.builder().id((long) i).name("genre" + i).build());
        }

        Games g = new Games(
                gamesId,
                games.getName(),
                games.getExplanation(),
                games.getMainImage(),
                games.getReleaseDate(),
                false,
                LocalDate.now(),
                getGamesGenre(games, genres),
                getGamesPlatform(games, platforms),
                0
        );

        return g;
    }


    private Set<GamesGenre> getGamesGenre(Games games, List<Genre> genres) {
        Set<GamesGenre> gamesGenres = new HashSet<>();

        for (Genre genre : genres) {
            gamesGenres.add(new GamesGenre(games, genre));
        }

        return gamesGenres;
    }

    private Set<GamesPlatform> getGamesPlatform(Games games, List<Platform> platforms) {
        Set<GamesPlatform> gamesPlatforms = new HashSet<>();

        for (Platform platform : platforms) {
            gamesPlatforms.add(new GamesPlatform(games, platform));
        }

        return gamesPlatforms;
    }

}
