package com.gmi.gameInfo.games.controller;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
                getGamesPlatform(sam, platforms)
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
