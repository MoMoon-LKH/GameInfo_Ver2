package com.gmi.gameInfo.member.service;

import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.domain.MemberToken;
import com.gmi.gameInfo.member.domain.dto.RegisterDto;
import com.gmi.gameInfo.member.exception.DuplicateMemberException;
import com.gmi.gameInfo.member.exception.NotFoundMemberException;
import com.gmi.gameInfo.member.repository.MemberRepository;
import com.gmi.gameInfo.member.repository.MemberTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberTokenRepository memberTokenRepository;

    @Transactional
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(NotFoundMemberException::new);
    }

    public Member findByLoginId(String loginId) {
        return memberRepository.findMemberByLoginId(loginId).orElseThrow(NotFoundMemberException::new);
    }

    public boolean duplicateLoginId(String loginId) {
        int count = memberRepository.countByLoginId(loginId);

        return count > 0;
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

    @Transactional
    public void saveRefreshToken(String loginId, MemberToken memberToken) {

        Member member = memberRepository.findMemberByLoginId(loginId)
                .orElseThrow(NotFoundMemberException::new);

        if (member.getMemberToken() != null) {
            member.getMemberToken().updateLoginRefreshToken(memberToken.getRefreshToken());
        } else {
            memberTokenRepository.save(memberToken);
            member.updateMemberToken(memberToken);
        }
    }

    @Transactional
    public void deleteMemberToken(String loginId) {

        Member member = memberRepository.findMemberByLoginId(loginId)
                .orElseThrow(NotFoundMemberException::new);

        MemberToken token = member.getMemberToken();
        member.updateMemberToken(null);
        memberTokenRepository.delete(token);
    }
}
