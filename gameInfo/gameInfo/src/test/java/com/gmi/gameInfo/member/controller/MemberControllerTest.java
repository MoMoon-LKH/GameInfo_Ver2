package com.gmi.gameInfo.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmi.gameInfo.exceptionHandler.GlobalExceptionHandler;
import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.domain.dto.RegisterDto;
import com.gmi.gameInfo.member.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Calendar;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(new MemberController(memberService, passwordEncoder))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("회원가입 테스트")
    void registerMember() throws Exception {
    
        //given
        Calendar cal = Calendar.getInstance();
        cal.set(1996,6,19);

        RegisterDto registerDto = RegisterDto.builder()
                .loginId("test")
                .password("123456")
                .name("테스트")
                .nickname("테스트 닉네임")
                .birthday(new Date(cal.getTimeInMillis()))
                .phoneNo("010-1234-5678")
                .email("test@asdf.com").build();

        given(memberService.registerMember(any(RegisterDto.class))).willReturn(Member.createMember(registerDto));

        //when
        
        //then
        mockMvc.perform(post("/api/members/register")
                    .content(objectMapper.writeValueAsString(registerDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("테스트"))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 아이디 - 중복 여부")
    void duplicateStatusLoginId() throws Exception {

        //given
        String loginId = "test";

        given(memberService.duplicateLoginId(loginId)).willReturn(true);

        //when
        ResultActions result = mockMvc.perform(get("/api/members/duplicate-loginId")
                .param("loginId", loginId)
        );

        //then
        result
                .andDo(print())
                .andExpect(status().isOk());

        String content = result.andReturn().getResponse().getContentAsString();

        Assertions.assertTrue(Boolean.parseBoolean(content));
    }

}
