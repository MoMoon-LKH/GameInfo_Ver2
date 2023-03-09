package com.gmi.gameInfo.member.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema( description = "로그인 Response Dto")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {

    private String message;
    private String accessToken;
    private MemberSimpleDto member;
}
