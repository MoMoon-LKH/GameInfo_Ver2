package com.gmi.gameInfo.games.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GamesCreateDto {

    private String name;

    private String explanation;

    private String mainImage;

}
