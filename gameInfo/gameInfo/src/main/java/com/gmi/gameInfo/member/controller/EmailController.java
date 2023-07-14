package com.gmi.gameInfo.member.controller;

import com.gmi.gameInfo.exceptionHandler.ErrorResponse;
import com.gmi.gameInfo.member.domain.AuthEmail;
import com.gmi.gameInfo.member.domain.dto.EmailDto;
import com.gmi.gameInfo.member.service.EmailService;
import com.gmi.gameInfo.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Api(tags = "Email", description = "이메일 인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;

    private final MemberService memberService;

    @Operation(summary = "인증 이메일 전송", description = "회원가입에 필요한 인증번호 메일 전송 API")
    @ApiResponse(responseCode = "200", description = "Email Send Success")
    @ApiResponse(responseCode = "409", description = "Duplicate Email Exception",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/authenticate")
    public ResponseEntity<?> sendAuthEmail(
            @RequestBody EmailDto emailDto) {

        memberService.duplicateEmail(emailDto.getEmail());

        AuthEmail authEmail = AuthEmail.createAuthEmail(emailDto.getEmail(), emailService.getAuthNum());
        emailService.sendAndSaveAuthEmail(authEmail);

        Map<String, Object> map = new HashMap<>();
        map.put("id", authEmail.getId());
        map.put("email", authEmail.getEmail());

        return ResponseEntity.ok(map);
    }

    @Operation(summary = "인증번호 확인", description = "인증번호 확인 API")
    @ApiResponse(responseCode = "200", description = "Confirm Success",
            content = @Content(schema = @Schema(implementation = Boolean.class)))
    @ApiResponse(responseCode = "400", description = "Difference Number Exception",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/verify-number/{id}")
    public ResponseEntity<?> verifyAuthenticateNumber(
            @Parameter(name = "id", description = "인증번호메일 id", in = ParameterIn.PATH, required = true)
            @PathVariable String id,
            @Parameter(name = "authNum", description = "인증번호", required = true)
            @RequestParam String authNum) {

        AuthEmail authEmail = emailService.findOneById(id);
        boolean checked = emailService.confirmAuthNum(authEmail, authNum);
        emailService.deleteById(id);

        return ResponseEntity.ok(checked);
    }
}
