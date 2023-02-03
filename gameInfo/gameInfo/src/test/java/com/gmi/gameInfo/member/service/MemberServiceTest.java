package com.gmi.gameInfo.member.service;

import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.domain.dto.RegisterDto;
import com.gmi.gameInfo.member.exception.DuplicateMemberException;
import com.gmi.gameInfo.member.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    MemberService memberService;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
    }


    @Test
    @DisplayName("회원가입 테스트")
    void registerMember() {

        //given
        RegisterDto registerDto = RegisterDto.builder()
                .loginId("test")
                .password("test")
                .name("test")
                .nickname("nickname")
                .email("email@naver.com")
                .phoneNo("01323232323")
                .birthday(Date.valueOf(LocalDate.of(1996, 6, 19)))
                .boolCertifiedEmail(true)
                .build();

        registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        Member member = Member.createMember(registerDto);

        given(memberRepository.save(any(Member.class))).willReturn(member);

        //when
        Member registerMember = memberService.save(Member.createMember(registerDto));

        //then
        assertEquals(registerMember.getLoginId(), registerDto.getLoginId());

    }
    
    @Test
    @DisplayName("회원 중복조회 시")
    void checkDuplicateMember() {
    
        //given
        RegisterDto registerDto = RegisterDto.builder()
                .loginId("test")
                .password("test")
                .name("test")
                .nickname("nickname")
                .email("email@naver.com")
                .phoneNo("01323232323")
                .birthday(Date.valueOf(LocalDate.of(1996, 6, 19)))
                .boolCertifiedEmail(true)
                .build();

        registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        Member member = Member.createMember(registerDto);

        given(memberRepository.findDuplicateMemberBYDto(registerDto)).willReturn(Optional.of(member));

        //when


        //then
        assertThrows(DuplicateMemberException.class, () -> {
            memberService.registerMember(registerDto);
        });

    }

    @Test
    @DisplayName("단일조회 실패 Exception")
    void notFoundMember() {

        //given

        //when

        //then
    }


    @Test
    @DisplayName("단일조회 테스트")
    void findOneById() {

        //given

        //when

        //then
    }
}
