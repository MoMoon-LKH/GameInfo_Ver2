package com.gmi.gameInfo.member.repository;

import com.gmi.gameInfo.member.domain.AuthEmail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface EmailRepository extends CrudRepository<AuthEmail, Long> {

}
