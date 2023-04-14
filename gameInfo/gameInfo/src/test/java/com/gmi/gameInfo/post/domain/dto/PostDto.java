package com.gmi.gameInfo.post.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Schema(description = "포스트 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    @ApiModelProperty(value = "제목", required = true)
    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    @ApiModelProperty(value = "내용", required = true)
    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    @ApiModelProperty(value = "이미지 아이디 리스트")
    private List<Long> imageIds;
}
