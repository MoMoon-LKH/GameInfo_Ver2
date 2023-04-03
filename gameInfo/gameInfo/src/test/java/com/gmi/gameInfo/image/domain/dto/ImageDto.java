package com.gmi.gameInfo.image.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema( description = "이미지 DTO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {

    private Long id;

    private String fileName;
}
