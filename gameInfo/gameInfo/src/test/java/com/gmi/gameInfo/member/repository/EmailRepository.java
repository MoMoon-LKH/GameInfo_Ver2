package com.gmi.gameInfo.member.repository;

import com.gmi.gameInfo.member.domain.AuthEmail;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface EmailRepository extends CrudRepository<AuthEmail, String> {

    Optional<AuthEmail> findAuthEmailByEmail(@Param("email") String email);

}
