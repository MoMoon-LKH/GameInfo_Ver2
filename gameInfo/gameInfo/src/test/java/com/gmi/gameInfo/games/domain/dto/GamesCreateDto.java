package com.gmi.gameInfo.games.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GamesCreateDto {


    private String name;

    private String explanation;

    private String mainImage;

    private LocalDate releaseDate;

    private List<Long> platformList = new ArrayList<>();

    private List<Long> genreList = new ArrayList<>();

}
