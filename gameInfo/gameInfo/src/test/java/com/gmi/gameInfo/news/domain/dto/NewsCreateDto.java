package com.gmi.gameInfo.news.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "News Dto for create")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsCreateDto {

    @ApiModelProperty(name = "제목", required = true)
    @NotBlank(message = "제목을 입력해주세요")
    private String title;
    
    @ApiModelProperty(name = "내용", required = true)
    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    @ApiModelProperty(name = "플랫폼 id", required = true)
    @NotNull(message = "플랫폼을 선택해주세요")
    private Long platformId;

    @ApiModelProperty(name = "이미지 아이디 리스트")
    private List<Long> imageIds;
}
