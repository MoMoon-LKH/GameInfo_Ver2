package com.gmi.gameInfo.member.service;

import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.domain.MemberToken;
import com.gmi.gameInfo.member.domain.dto.RegisterDto;
import com.gmi.gameInfo.member.exception.DuplicateMemberException;
import com.gmi.gameInfo.member.exception.NotFoundMemberException;
import com.gmi.gameInfo.member.repository.MemberRepository;
import com.gmi.gameInfo.member.repository.MemberTokenRepository;
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

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    MemberTokenRepository memberTokenRepository;

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

        given(memberRepository.findDuplicateMemberByDto(registerDto)).willThrow(DuplicateMemberException.class);

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
        given(memberRepository.findById(any(Long.class))).willThrow(NotFoundMemberException.class);

        //when


        //then
        assertThrows(NotFoundMemberException.class, () -> {
            memberService.findById(0L);
        });
    }


    @Test
    @DisplayName("단일조회 테스트")
    void findOneById() {

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

        //when
        given(memberRepository.findById(any(Long.class))).willReturn(Optional.of(member));

        //then
        assertNotNull(memberService.findById(1L));
    }
    
    @Test
    @DisplayName("로그인 시 토큰 등록")
    void loginMemberToken() {
    
        //given
        MemberToken memberToken = MemberToken.builder()
                .refreshToken("test")
                .createDate(new java.util.Date())
                .build();

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

        given(memberTokenRepository.save(memberToken)).willReturn(memberToken);
        given(memberRepository.findMemberByLoginId(any(String.class))).willReturn(Optional.of(member));

        //when
        memberService.saveRefreshToken(member.getLoginId(), memberToken);

        //then
        assertEquals("test", member.getMemberToken().getRefreshToken());
    }
}
