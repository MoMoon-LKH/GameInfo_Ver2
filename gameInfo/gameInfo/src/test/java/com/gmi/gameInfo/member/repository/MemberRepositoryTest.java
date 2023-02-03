package com.gmi.gameInfo.member.repository;

import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.domain.dto.RegisterDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    PasswordEncoder passwordEncoder;


    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    @Rollback
    @DisplayName("저장 테스트")
    void register_member() {

        //given
        Calendar cal = Calendar.getInstance();
        cal.set(1996, Calendar.JULY,19);

        RegisterDto registerDto = RegisterDto.builder()
                .loginId("test")
                .password(passwordEncoder.encode("123456"))
                .name("테스트")
                .nickname("테스트 닉네임")
                .birthday(new Date(cal.getTimeInMillis()))
                .phoneNo("01012345678")
                .email("test@asdf.com").build();

        Member member = Member.createMember(registerDto);

        // when
        Member saveMember = memberRepository.save(member);

        //then
        assertEquals(member.getId(), saveMember.getId());
    }

    @Test
    @Rollback
    @DisplayName("단일조회")
    void findById() {

        //given
        Calendar cal = Calendar.getInstance();
        cal.set(1996,6,19);

        RegisterDto registerDto = RegisterDto.builder()
                .loginId("test")
                .password(passwordEncoder.encode("123456"))
                .name("테스트")
                .nickname("테스트 닉네임")
                .birthday(new Date(cal.getTimeInMillis()))
                .phoneNo("01012345678")
                .email("test@asdf.com").build();

        Member member = Member.createMember(registerDto);
        memberRepository.save(member);

        //when
        Optional<Member> findMember = memberRepository.findById(member.getId());

        //then
        assertEquals(findMember.orElseGet(null), member);
    }

    @Test
    @Rollback
    @DisplayName("회원 중복조회")
    void findDuplicateMember() {

        //given
        Calendar cal = Calendar.getInstance();
        cal.set(1996,6,19);

        RegisterDto registerDto = RegisterDto.builder()
                .loginId("test")
                .password(passwordEncoder.encode("123456"))
                .name("테스트")
                .nickname("테스트 닉네임")
                .birthday(new Date(cal.getTimeInMillis()))
                .phoneNo("01012345678")
                .email("test@asdf.com").build();
        Member member = Member.createMember(registerDto);
        memberRepository.save(member);

        //when
        Optional<Member> dupMember = memberRepository.findDuplicateMemberByDto(registerDto);

        //then
        assertNotNull(dupMember.orElseGet(null));
    }
    
    @Test
    @Rollback
    @DisplayName("이메일로 회원조회")
    void findOneByEmail() {
    
        //given
        Calendar cal = Calendar.getInstance();
        cal.set(1996,6,19);

        RegisterDto registerDto = RegisterDto.builder()
                .loginId("test")
                .password(passwordEncoder.encode("123456"))
                .name("테스트")
                .nickname("테스트 닉네임")
                .birthday(new Date(cal.getTimeInMillis()))
                .phoneNo("01012345678")
                .email("test@asdf.com").build();
        Member member = Member.createMember(registerDto);
        memberRepository.save(member);

        //when
        Optional<Member> dupMember = memberRepository.findMemberByEmail(registerDto.getEmail());
        
        //then
        assertEquals(member, dupMember.orElseGet(null));
    }
}
