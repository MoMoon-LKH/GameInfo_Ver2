package com.gmi.gameInfo.genre.repository;

import com.gmi.gameInfo.genre.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
}
