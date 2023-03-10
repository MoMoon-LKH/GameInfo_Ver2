package com.gmi.gameInfo.member.controller;


import com.gmi.gameInfo.jwt.TokenProvider;
import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.domain.MemberToken;
import com.gmi.gameInfo.member.domain.dto.LoginDto;
import com.gmi.gameInfo.member.domain.dto.LoginResponseDto;
import com.gmi.gameInfo.member.domain.dto.MemberSimpleDto;
import com.gmi.gameInfo.member.service.MemberService;
import com.gmi.gameInfo.member.service.MemberTokenService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;

@Api(tags = "Auth Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberService memberService;

    private final MemberTokenService memberTokenService;

    @Value("${jwt.refresh-validity-date}")
    private int refreshDays;

    @Operation(summary = "로그인", description = "회원 로그인 API")
    @ApiResponse(responseCode = "200", description = "Login Success",
            content = @Content(schema = @Schema(implementation = LoginResponseDto.class)),
            headers = {
                @Header(name = "Authorization", description = "JWT Token", schema = @Schema(implementation = String.class))
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
        String refresh = tokenProvider.createRefreshToken(authentication, 0);


        saveMemberToken(loginDto.getLoginId(), refresh);

        Member member = memberService.findByLoginId(loginDto.getLoginId());

        String accessToken = addTokenFrontString(access);
        response.setHeader("Authorization", accessToken);
        response.addCookie(createRefreshCookie(refresh, 60 * 60 * 24 * refreshDays));


        LoginResponseDto loginResponse = LoginResponseDto.builder()
                .message("로그인 되었습니다")
                .accessToken(accessToken)
                .member(new MemberSimpleDto(member))
                .build();

        return ResponseEntity.ok(loginResponse);
    }


    @Operation(summary = "로그아웃", description = "로그아웃 API")
    @ApiResponse(responseCode = "200", description = "Logout Success",
        content = @Content(schema = @Schema(implementation = LoginResponseDto.class)))
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @AuthenticationPrincipal UserDetails userDetails,
            HttpServletResponse response
    ) {

        memberService.deleteMemberToken(userDetails.getUsername());

        response.addCookie(createRefreshCookie(null, 0));

        LoginResponseDto loginResponse = LoginResponseDto.builder()
                .message("로그아웃 되었습니다")
                .build();

        return ResponseEntity.ok(loginResponse);
    }

    @Operation(summary = "토큰 재발급", description = "access 및 refresh 토큰 재발급")
    @ApiResponse(responseCode = "200", description = "Reissue Success",
        content = @Content(schema = @Schema(implementation = LoginResponseDto.class)),
        headers = {
                @Header(name = "Authorization", description = "JWT Token", schema = @Schema(implementation = String.class))
        }
    )
    @PostMapping("/reissue-token")
    public ResponseEntity<?> reissueToken(
            @Parameter(name = "memberId", description = "회원 일련번호", required = true)
            @RequestParam Long memberId,
            @CookieValue(value = "gameInfo") Cookie cookie,
            HttpServletResponse response
    ) {

        Member member = memberService.findById(memberId);
        String prevRefresh = cookie.getValue();
        Authentication authentication = tokenProvider.getRefreshAuthentication(prevRefresh);

        String access = addTokenFrontString(tokenProvider.createAccessToken(authentication));
        String refresh = tokenProvider.createRefreshToken(authentication, cookie.getMaxAge());

        response.setHeader("Authorization", access);
        response.addCookie(createRefreshCookie(refresh, cookie.getMaxAge()));


        LoginResponseDto loginResponse = LoginResponseDto.builder()
                .message("정상적으로 발행되었습니다")
                .member(new MemberSimpleDto(member))
                .accessToken(access)
                .build();

        return ResponseEntity.ok(loginResponse);
    }



    private String addTokenFrontString(String token) {
        return "Bearer " + token;
    }

    private Cookie createRefreshCookie(String refresh, int maxAge) {

        Cookie cookie = new Cookie("gameInfo", refresh);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);

        return cookie;
    }

    private void saveMemberToken(String loginId, String refreshToken) {
        MemberToken memberToken = MemberToken.builder()
                .refreshToken(refreshToken)
                .createDate(new Date()).build();

        memberService.saveRefreshToken(loginId, memberToken);
    }
}
