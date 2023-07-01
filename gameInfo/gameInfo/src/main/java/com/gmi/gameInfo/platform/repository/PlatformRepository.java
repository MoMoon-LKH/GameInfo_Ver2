package com.gmi.gameInfo.platform.repository;

import com.gmi.gameInfo.platform.domain.Platform;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {

    List<Platform> findAllByIdIn(@Param("id") List<Long> ids);
}
