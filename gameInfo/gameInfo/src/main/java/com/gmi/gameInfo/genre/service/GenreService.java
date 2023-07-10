package com.gmi.gameInfo.genre.service;

import com.gmi.gameInfo.genre.domain.Genre;
import com.gmi.gameInfo.genre.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    @Transactional
    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    public List<Genre> findAllByIdsIn(List<Long> ids) {
        return genreRepository.findAllByIdIn(ids);
    }
}
