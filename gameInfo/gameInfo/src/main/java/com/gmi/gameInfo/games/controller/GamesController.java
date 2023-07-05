package com.gmi.gameInfo.games.controller;

import com.gmi.gameInfo.exceptionHandler.ErrorResponse;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Game", description = "게임 관련 API")
@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GamesController {

    private final GamesService gamesService;

    private final GenreService genreService;

    private final PlatformService platformService;


    @Operation(summary = "게임정보 등록", description = "게임정보 등록 API (관리자 계정만 가능)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "등록 성공",
                    content = @Content(schema = @Schema(implementation = GamesDto.class))),
            @ApiResponse(responseCode = "404", description = "조회 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "동일한 게임 이름 존재",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    }
    )
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


    @Operation(summary = "게임 리스트 조회", description = "게임정보 리스트 조회 API (페이징)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = GamesListDto.class))))
    })
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

    @Operation(summary = "게임 단일 조회", description = "id를 통한 게임 관련 정보 단일 조회 (이후 추가적으로 post와 연결하여 조회항목 늘릴예정)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = GamesDto.class)))
    })
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
