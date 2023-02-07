package com.gmi.gameInfo.jwt.service;

import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.exception.LoginFailException;
import com.gmi.gameInfo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findMemberByLoginId(username)
                .map(this::createUser)
                .orElseThrow(LoginFailException::new);
    }


    private User createUser(Member member) {
        List<GrantedAuthority> grantedAuthority = Collections
                .singletonList(new SimpleGrantedAuthority("ROLE_" + member.getRoleType()));

        return new User(member.getLoginId(), member.getPassword(), grantedAuthority);
    }
}
