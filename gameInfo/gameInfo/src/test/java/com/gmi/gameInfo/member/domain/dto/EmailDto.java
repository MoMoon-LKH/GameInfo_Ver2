package com.gmi.gameInfo.member.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(description = "이메일 DTO")
public class EmailDto {

    @ApiModelProperty(value = "이메일 id")
    private String id;
    
    @ApiModelProperty(value = "이메일")
    private String email;

    @ApiModelProperty(value = "인증번호")
    private String authNum;
}
