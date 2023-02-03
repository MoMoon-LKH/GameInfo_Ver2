package com.gmi.gameInfo.member.controller;

import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.domain.dto.MemberDto;
import com.gmi.gameInfo.member.domain.dto.RegisterDto;
import com.gmi.gameInfo.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    private final PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public ResponseEntity<?> registerMembers(
            @RequestBody RegisterDto registerDto
            ) {

        registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        Member member = memberService.registerMember(registerDto);

        return ResponseEntity.ok(new MemberDto(member));
    }
}
