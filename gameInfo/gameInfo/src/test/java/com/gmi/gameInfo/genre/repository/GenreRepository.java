package com.gmi.gameInfo.genre.repository;

import com.gmi.gameInfo.genre.domain.Genre;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    List<Genre> findAllByIdIn(@Param("id") List<Long> ids);

}
