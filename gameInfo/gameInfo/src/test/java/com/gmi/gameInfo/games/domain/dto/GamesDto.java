package com.gmi.gameInfo.games.domain.dto;

import com.gmi.gameInfo.games.domain.Games;
import com.gmi.gameInfo.games.domain.GamesGenre;
import com.gmi.gameInfo.games.domain.GamesPlatform;
import com.gmi.gameInfo.genre.domain.dto.GenreDto;
import com.gmi.gameInfo.platform.domain.dto.PlatformDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GamesDto {


    private Long id;

    private String name;

    private String explanation;

    private String mainImage;

    private LocalDate releaseDate;

    private boolean deleteYn;

    private List<GenreDto> genreList;

    private List<PlatformDto> platformList;


    public GamesDto(Games games) {
        this.id = games.getId();
        this.name = games.getName();
        this.mainImage = games.getMainImage();
        this.deleteYn = games.isDeleteYn();
        this.releaseDate = games.getReleaseDate();
        this.genreList = convertGenreDto(games.getGenres());
        this.platformList = convertPlatformDto(games.getPlatforms());
    }

    protected List<GenreDto> convertGenreDto(Set<GamesGenre> genreSet) {

        List<GenreDto> list = new ArrayList<>();

        for (GamesGenre gamesGenre : genreSet) {
            list.add(new GenreDto(gamesGenre.getGenre()));
        }

        return list;
    }

    protected List<PlatformDto> convertPlatformDto(Set<GamesPlatform> platformSet) {
        List<PlatformDto> list = new ArrayList<>();

        for (GamesPlatform gamesPlatform : platformSet) {
            list.add(new PlatformDto(gamesPlatform.getPlatform()));
        }

        return list;
    }
}
