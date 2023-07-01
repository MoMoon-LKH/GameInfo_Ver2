package com.gmi.gameInfo.games.domain.dto;

import com.gmi.gameInfo.games.domain.Games;
import com.gmi.gameInfo.games.domain.GamesGenre;
import com.gmi.gameInfo.games.domain.GamesPlatform;
import com.gmi.gameInfo.genre.domain.Genre;
import com.gmi.gameInfo.genre.domain.dto.GenreDto;
import com.gmi.gameInfo.platform.domain.Platform;
import com.gmi.gameInfo.platform.domain.dto.PlatformDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GamesListDto {

    private Long id;

    private String name;

    private String mainImage;

    private LocalDate releaseDate;

    private List<GenreDto> genreList;

    private List<PlatformDto> platformList;


    public GamesListDto(Games games) {
        this.id = games.getId();
        this.name = games.getName();
        this.mainImage = games.getMainImage();
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
