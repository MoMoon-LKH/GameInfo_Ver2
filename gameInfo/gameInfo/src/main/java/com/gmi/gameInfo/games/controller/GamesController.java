package com.gmi.gameInfo.games.controller;

import com.gmi.gameInfo.games.domain.Games;
import com.gmi.gameInfo.games.domain.dto.GamesCreateDto;
import com.gmi.gameInfo.games.domain.dto.GamesDto;
import com.gmi.gameInfo.games.domain.dto.GamesFindDto;
import com.gmi.gameInfo.games.domain.dto.GamesListDto;
import com.gmi.gameInfo.games.service.GamesService;
import com.gmi.gameInfo.genre.domain.Genre;
import com.gmi.gameInfo.genre.service.GenreService;
import com.gmi.gameInfo.platform.domain.Platform;
import com.gmi.gameInfo.platform.service.PlatformService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GamesController {

    private final GamesService gamesService;

    private final GenreService genreService;

    private final PlatformService platformService;


    @PostMapping("")
    public ResponseEntity<?> createGame(
            @RequestBody GamesCreateDto createDto
    ) {

        Games games = Games.createGames(createDto);
        List<Genre> genres = new ArrayList<>();
        List<Platform> platforms = new ArrayList<>();

        if (createDto.getGenreList() != null && createDto.getGenreList().size() > 0) {
            genres = genreService.findAllByIdsIn(createDto.getGenreList());
        }

        if (createDto.getPlatformList() != null && createDto.getPlatformList().size() > 0) {
            platforms = platformService.findAllByIdsIn(createDto.getPlatformList());
        }

        gamesService.save(games, platforms, genres);

        Games find = gamesService.findDetailById(games.getId());

        GamesDto gamesDto = new GamesDto(find);


        return ResponseEntity.status(HttpStatus.CREATED).body(gamesDto);
    }

    @GetMapping("/list")
    public ResponseEntity<?> findListByPage(
            @RequestParam String search,
            @RequestParam(value="platformId", required = false) List<Long> platformIds,
            @RequestParam(value = "genreId", required = false) List<Long> genreIds,
            @PageableDefault(size = 30) Pageable pageable
    ) {

        GamesFindDto findDto = GamesFindDto.builder()
                .search(search)
                .platformIds(platformIds)
                .genreIds(genreIds)
                .build();

        List<Games> find = gamesService.findByPageable(findDto, pageable);
        List<GamesListDto> dtoList = new ArrayList<>();

        for (Games game : find) {
            dtoList.add(new GamesListDto(game));
        }

        return ResponseEntity.ok(dtoList);
    }



    @GetMapping("/{id}")
    public ResponseEntity<?> findGameById(
            @PathVariable("id") Long id
    ) {

        Games games = gamesService.findDetailById(id);

        GamesDto gamesDto = new GamesDto(games);

        // 추가 상세 정보가 있다면 조회

        return ResponseEntity.ok(gamesDto);
    }

}
