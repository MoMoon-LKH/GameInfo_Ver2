package com.gmi.gameInfo.games.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GamesCreateDto {


    @NotBlank(message = "게임 이름을 입력해주세요")
    @Size(min = 1, max = 30)
    private String name;

    private String explanation;

    private String mainImage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    private List<Long> platformList = new ArrayList<>();

    private List<Long> genreList = new ArrayList<>();

}
