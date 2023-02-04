package com.gmi.gameInfo.member.repository;

import com.gmi.gameInfo.member.domain.MemberToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberTokenRepository extends JpaRepository<MemberToken, Long> {

}
