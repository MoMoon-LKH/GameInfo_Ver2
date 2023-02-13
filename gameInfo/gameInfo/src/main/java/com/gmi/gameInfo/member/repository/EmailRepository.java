package com.gmi.gameInfo.member.repository;

import com.gmi.gameInfo.member.domain.AuthEmail;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EmailRepository extends CrudRepository<AuthEmail, Long> {

    Optional<AuthEmail> findAuthEmailByEmail(@Param("email") String email);

}
