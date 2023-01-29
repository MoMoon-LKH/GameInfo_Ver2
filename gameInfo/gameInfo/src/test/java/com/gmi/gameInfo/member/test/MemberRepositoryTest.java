package com.gmi.gameInfo.member.test;

import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.domain.dto.RegisterDto;
import com.gmi.gameInfo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;


    @Test
    @Rollback
    @DisplayName("회원가입 테스트")
    void register_member() {

        //given
        Calendar cal = Calendar.getInstance();
        cal.set(1996,6,19);

        RegisterDto registerDto = RegisterDto.builder()
                .loginId("test")
                .password("123456")
                .name("테스트")
                .nickname("테스트 닉네임")
                .birthday(new Date(cal.getTimeInMillis()))
                .phoneNo("01012345678")
                .email("test@asdf.com").build();

        Member member = Member.registerMember(registerDto);

        // when
        Member saveMember = memberRepository.save(member);

        //then
        assertEquals(member.getId(), saveMember.getId());
    }

}
