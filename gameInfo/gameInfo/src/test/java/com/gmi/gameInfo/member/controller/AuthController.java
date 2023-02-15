package com.gmi.gameInfo.member.controller;


import com.gmi.gameInfo.jwt.TokenProvider;
import com.gmi.gameInfo.member.domain.MemberToken;
import com.gmi.gameInfo.member.domain.dto.LoginDto;
import com.gmi.gameInfo.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ResponseHeader;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "Auth Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberService memberService;

    @Value("${jwt.refresh-validity-date}")
    private int refreshDays;

    @Operation(summary = "로그인", description = "회원 로그인 API")
    @ApiResponse(code = 200, message = "Login Success",
            responseHeaders = {
                @ResponseHeader(name = "Authorization", description = "JWT Token", response = String.class)
            })
    @PostMapping("/login")
    public ResponseEntity<?> authorize(
            @Valid @RequestBody LoginDto loginDto,
            HttpServletResponse response
    ) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getLoginId(), loginDto.getPassword());

        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        String access = tokenProvider.createAccessToken(authentication);
        String refresh = tokenProvider.createRefreshToken(authentication);

        saveMemberToken(loginDto.getLoginId(), refresh);

        String accessToken = addTokenFrontString(access);
        response.setHeader("Authorization", accessToken);
        response.addCookie(createRefreshCookie(refresh));

        Map<String, Object> map = new HashMap<>();
        map.put("message", "로그인 성공");
        map.put("accessToken", accessToken);

        return ResponseEntity.ok(map);
    }


    private String addTokenFrontString(String token) {
        return "Bearer " + token;
    }

    private Cookie createRefreshCookie(String refresh) {

        Cookie cookie = new Cookie("gameInfo", refresh);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * refreshDays);

        return cookie;
    }

    private void saveMemberToken(String loginId, String refreshToken) {
        MemberToken memberToken = MemberToken.builder()
                .refreshToken(refreshToken)
                .createDate(new Date()).build();

        memberService.saveRefreshToken(loginId, memberToken);
    }
}
