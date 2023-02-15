package com.gmi.gameInfo.member.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Schema(description = "로그인 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    @ApiModelProperty(value = "로그인 아이디", required = true)
    @NotBlank(message = "아이디를 입력해주세요")
    private String loginId;

    @ApiModelProperty(value = "로그인 패스워드", required = true)
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;
}
