package com.gmi.gameInfo.member.service;

import com.gmi.gameInfo.member.domain.MemberToken;
import com.gmi.gameInfo.member.repository.MemberTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberTokenService {

    private final MemberTokenRepository memberTokenRepository;

    public MemberToken save(MemberToken memberToken) {
        return memberTokenRepository.save(memberToken);
    }
}
