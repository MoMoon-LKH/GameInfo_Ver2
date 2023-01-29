package com.gmi.gameInfo.member.service;

import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    @Transactional
    public Member save(Member member) {
        return memberRepository.save(member);
    }

}
