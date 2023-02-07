package com.gmi.gameInfo.member.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    
    @NotBlank(message = "아이디를 입력해주세요")
    private String loginId;
    
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;
}
