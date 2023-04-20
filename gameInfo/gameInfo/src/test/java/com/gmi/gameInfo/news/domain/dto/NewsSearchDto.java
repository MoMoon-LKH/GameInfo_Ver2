package com.gmi.gameInfo.news.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Schema( description = "news 검색 dto")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsSearchDto {


    @ApiModelProperty(name = "플랫폼 id", required = true)
    @NotNull
    private Long platformId;

    private String searchWord;

    private String searchSelect;
}
