package com.gmi.gameInfo.member.service;

import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.domain.dto.RegisterDto;
import com.gmi.gameInfo.member.exception.DuplicateMemberException;
import com.gmi.gameInfo.member.exception.NotFoundMemberException;
import com.gmi.gameInfo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    @Transactional
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(NotFoundMemberException::new);
    }

    @Transactional
    public Member registerMember(RegisterDto registerDto) {

        Optional<Member> dupMember = memberRepository
                .findDuplicateMemberByDto(registerDto);

        if (dupMember.isPresent()) {
            throw  new DuplicateMemberException();
        }

        return memberRepository.save(Member.createMember(registerDto));
    }
}
