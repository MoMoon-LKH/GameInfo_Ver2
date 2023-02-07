package com.gmi.gameInfo.member.repository;

import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.domain.dto.RegisterDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m From Member m " +
            "WHERE (m.name = :#{#dto.name} AND m.birthday = :#{#dto.birthday}) " +
            "OR m.email = :#{#dto.email}")
    Optional<Member> findDuplicateMemberByDto(@Param("dto") RegisterDto registerDto);

    Optional<Member> findMemberByEmail(@Param("email") String email);

    Optional<Member> findMemberByLoginId(@Param("loginId") String loginId);
}
