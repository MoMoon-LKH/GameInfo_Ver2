package com.gmi.gameInfo.platform.repository;

import com.gmi.gameInfo.platform.domain.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {

}
