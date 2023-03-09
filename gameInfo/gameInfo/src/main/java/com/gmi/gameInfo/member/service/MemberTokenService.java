package com.gmi.gameInfo.member.service;

import com.gmi.gameInfo.member.domain.MemberToken;
import com.gmi.gameInfo.member.repository.MemberTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberTokenService {

    private final MemberTokenRepository memberTokenRepository;

    @Transactional
    public MemberToken save(MemberToken memberToken) {
        return memberTokenRepository.save(memberToken);
    }

    @Transactional
    public void delete(MemberToken memberToken) {
        memberTokenRepository.delete(memberToken);
    }

}
