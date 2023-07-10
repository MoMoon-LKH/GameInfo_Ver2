package com.gmi.gameInfo.games.service;

import com.gmi.gameInfo.games.domain.Games;
import com.gmi.gameInfo.games.domain.GamesPlatform;
import com.gmi.gameInfo.games.domain.dto.GamesCreateDto;
import com.gmi.gameInfo.games.repository.GamesGenreRepository;
import com.gmi.gameInfo.games.repository.GamesPlatformRepository;
import com.gmi.gameInfo.games.repository.GamesRepository;
import com.gmi.gameInfo.genre.domain.Genre;
import com.gmi.gameInfo.platform.domain.Platform;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
public class GamesServiceTest {

    @Mock
    private GamesRepository gamesRepository;

    @Mock
    private GamesPlatformRepository gamesPlatformRepository;

    @Mock
    private GamesGenreRepository gamesGenreRepository;

    @InjectMocks
    private GamesService gamesService;




    @Test
    @DisplayName("게임 저장 - (Genre || Platform) List가 size 0인 상태로 들어갈 경우")
    void save_EmptyGenreOrPlatformList() {

        //given
        GamesCreateDto createDto = GamesCreateDto.builder()
                .name("test")
                .explanation("explain")
                .mainImage("main")
                .releaseDate(LocalDate.now())
                .build();


        Games games = Games.createGames(createDto);
        List<Genre> genres = new ArrayList<>();
        List<Platform> platforms = new ArrayList<>();

        given(gamesRepository.save(any())).willReturn(games);

        //when
        Games save = gamesService.save(games, platforms, genres);

        //then
        assertEquals("test", save.getName());
        assertEquals(0, games.getPlatforms().size());
        assertEquals(0, games.getGenres().size());

    }
    
    
    @Test
    @DisplayName("게임 저장 - 성공")
    void save_success() {

        //given
        GamesCreateDto createDto = GamesCreateDto.builder()
                .name("test")
                .explanation("ex")
                .mainImage("main")
                .releaseDate(LocalDate.now())
                .build();
        Games games = Games.createGames(createDto);

        List<Genre> genres = new ArrayList<>();
        genres.add(Genre.builder().id(1L).name("genre1").build());
        genres.add(Genre.builder().id(2L).name("genre2").build());

        List<Platform> platforms = new ArrayList<>();
        platforms.add(Platform.builder().id(1L).name("platform1").build());

        given(gamesRepository.save(any())).willReturn(games);

        //when
        Games save = gamesService.save(games, platforms, genres);

        //then
        assertEquals(2, save.getGenres().size());
        assertEquals(1, save.getPlatforms().size());
    }

    @Test
    @DisplayName("게임 삭제")
    void delete_games() {
    
        //given
        GamesCreateDto createDto = GamesCreateDto.builder()
                .name("test")
                .explanation("ex")
                .mainImage("main")
                .releaseDate(LocalDate.now())
                .build();
        Games games = Games.createGames(createDto);

        //when
        gamesService.delete(games);
        
        //then
        assertTrue(games.isDeleteYn());
    }



}


