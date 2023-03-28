package com.gmi.gameInfo.category.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    @ApiModelProperty(value = "카테고리 일련번호")
    private Long id;

    @ApiModelProperty(value = "이름")
    private String name;
    
    @ApiModelProperty(value = "카테고리 부모 일련번호")
    private Long parentId;
}
