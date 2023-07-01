package com.gmi.gameInfo.games.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GamesFindDto {

    private String search;

    private List<Long> platformIds = new ArrayList<>();

    private List<Long> genreIds = new ArrayList<>();

}
