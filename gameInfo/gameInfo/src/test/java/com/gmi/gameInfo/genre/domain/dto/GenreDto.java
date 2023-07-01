package com.gmi.gameInfo.genre.domain.dto;

import com.gmi.gameInfo.genre.domain.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {

    private Long id;

    private String name;

    public GenreDto(Genre genre) {
        this.id = genre.getId();
        this.name = genre.getName();
    }
}
