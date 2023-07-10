package com.gmi.gameInfo.games.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(name = "게임명", value = "젤다의 전설")
    private String name;

    @ApiModelProperty(name = "게임 설명", value = "이 게임은 ~", allowEmptyValue = true)
    private String explanation;

    @ApiModelProperty(name = "게임 메인이미지 Path", value = "/cdsdw-1", allowEmptyValue = true)
    private String mainImage;

    @ApiModelProperty(name = "게임 발매일", value = "2023-07-10", allowEmptyValue = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @ApiModelProperty(name = "지원 플랫폼 ids", value = "[1, 2, 3]", allowEmptyValue = true)
    private List<Long> platformList = new ArrayList<>();

    @ApiModelProperty(name = "게임 장르 ids", value = "[1, 2, 3]", allowEmptyValue = true)
    private List<Long> genreList = new ArrayList<>();

    @ApiModelProperty(name = "동시성 확인 version (업데이트시 필수 항목)", value = "1", allowEmptyValue = true)
    private int version;

}
