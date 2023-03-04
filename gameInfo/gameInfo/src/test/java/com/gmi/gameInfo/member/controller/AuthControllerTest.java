package com.gmi.gameInfo.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmi.gameInfo.jwt.TokenProvider;
import com.gmi.gameInfo.jwt.service.CustomUserDetailService;
import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.domain.dto.LoginDto;
import com.gmi.gameInfo.member.domain.dto.RegisterDto;
import com.gmi.gameInfo.member.service.MemberService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Calendar;
import java.util.Date;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("dev")
public class AuthControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    @Rollback
    @DisplayName("로그인 - 실패")
    void failLogin() throws Exception {
    
        //given
        Calendar cal = Calendar.getInstance();
        cal.set(1996,6,19);
        RegisterDto registerDto = RegisterDto.builder()
                .loginId("test123")
                .password("123456")
                .name("테스트")
                .nickname("테스트 닉네임")
                .birthday(new Date(cal.getTimeInMillis()))
                .phoneNo("01012345678")
                .email("test@asdfs.com").build();

        LoginDto loginDto = LoginDto.builder()
                .loginId("test123")
                .password("test123")
                .build();

        //when


        //then
        mvc.perform(post("/api/auth/login")
                        .content(objectMapper.writeValueAsString(loginDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Rollback
    @Disabled
    @DisplayName("로그인 - 성공")
    void login() throws Exception{
        //given
        Calendar cal = Calendar.getInstance();
        cal.set(1996,6,19);
        RegisterDto registerDto = RegisterDto.builder()
                .loginId("test123")
                .password("123456")
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
}
