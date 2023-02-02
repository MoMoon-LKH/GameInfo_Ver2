package com.gmi.gameInfo.member.service;

import com.gmi.gameInfo.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @MockBean
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;
    
    
    @Test
    @DisplayName("회원가입 테스트")
    void registerMember() {
    
        //given
    
        //when
        
        //then
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
