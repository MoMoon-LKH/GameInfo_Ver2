package com.gmi.gameInfo.member.controller;

import com.gmi.gameInfo.exceptionHandler.ErrorResponse;
import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.domain.dto.MemberDto;
import com.gmi.gameInfo.member.domain.dto.RegisterDto;
import com.gmi.gameInfo.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Member Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    private final PasswordEncoder passwordEncoder;


    @Operation(summary = "회원가입", description = "회원가입 API")
    @ApiResponse(responseCode = "201", description = "회원가입 성공",
            content = @Content(schema = @Schema(implementation = MemberDto.class)))
    @ApiResponse(responseCode = "409", description = "회원 중복 에러",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/register")
    public ResponseEntity<?> registerMembers(
            @RequestBody RegisterDto registerDto
            ) {

        registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        Member member = memberService.registerMember(registerDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MemberDto(member));
    }

    @Operation(summary = "로그인 중복여부", description = "로그인 중복여부 확인\n 중복이면 true 아니라면 false")
    @ApiResponse(responseCode = "200", description = "중복여부",
        content = @Content(schema = @Schema(implementation = Boolean.class))
    )
    @GetMapping("/duplicate-loginId")
    public ResponseEntity<?> duplicateStatusLoginId(
        @Parameter(name = "loginId", description = "로그인 아이디", required = true)
        @RequestParam String loginId
    ) {
        boolean bool = memberService.duplicateLoginId(loginId);
        return ResponseEntity.ok(bool);
    }
}
