package com.gmi.gameInfo.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.domain.MemberToken;
import com.gmi.gameInfo.member.domain.dto.LoginDto;
import com.gmi.gameInfo.member.domain.dto.RegisterDto;
import com.gmi.gameInfo.member.service.MemberService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Calendar;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("dev")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    @Rollback
    @Order(2)
    @DisplayName("로그인 - 실패")
    void failLogin() throws Exception {
    
        //given
        LoginDto loginDto = LoginDto.builder()
                .loginId("test123")
                .password("test1234")
                .build();

        //when


        //then
        mvc.perform(post("/api/auth/login")
                .content(objectMapper.writeValueAsString(loginDto))
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Rollback
    @Order(1)
    @DisplayName("로그인 - 성공")
    void login() throws Exception{
        //given
        Calendar cal = Calendar.getInstance();
        cal.set(1996,6,19);
        RegisterDto registerDto = RegisterDto.builder()
                .loginId("test123")
                .password(passwordEncoder.encode("123456"))
                .name("테스트")
                .nickname("테스트 닉네임")
                .birthday(new Date(cal.getTimeInMillis()))
                .phoneNo("01012345678")
                .email("test@asdfs.com")
                .boolCertifiedEmail(true).build();

        LoginDto loginDto = LoginDto.builder()
                .loginId("test123")
                .password("123456")
                .build();

        Member member = memberService.registerMember(registerDto);


        //when
        ResultActions actions = mvc.perform(post("/api/auth/login")
                .content(objectMapper.writeValueAsString(loginDto))
                .contentType(MediaType.APPLICATION_JSON)
        );

        //result
        actions
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Rollback
    @Order(3)
    @WithMockUser(username = "test1234", password = "123456", roles = {"USER"})
    @DisplayName("로그아웃 - 성공")
    void logoutSuccess() throws Exception {
        //given
        Calendar cal = Calendar.getInstance();
        cal.set(1996,6,20);
        RegisterDto registerDto = RegisterDto.builder()
                .loginId("test1234")
                .password(passwordEncoder.encode("123456"))
                .name("테스트")
                .nickname("테스트 닉네임")
                .birthday(new Date(cal.getTimeInMillis()))
                .phoneNo("01012345678")
                .email("test@asdfsa.com")
                .boolCertifiedEmail(true).build();

        Member member = memberService.registerMember(registerDto);
        MemberToken memberToken = MemberToken.builder()
                .refreshToken("refresh")
                .createDate(new Date())
                .build();
        memberService.saveRefreshToken(member.getLoginId(), memberToken);

        //when
        ResultActions actions = mvc.perform(post("/api/auth/logout")
        );


        //then
        actions
                .andDo(print())
                .andExpect(status().isOk());
    }
}
