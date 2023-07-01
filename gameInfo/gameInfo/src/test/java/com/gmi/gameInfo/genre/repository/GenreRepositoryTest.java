package com.gmi.gameInfo.genre.repository;

import com.gmi.gameInfo.config.TestConfig;
import com.gmi.gameInfo.genre.domain.Genre;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestConfig.class)
public class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;


    @Test
    @DisplayName("Genre - List<Long> 에 해당되는 id의 리스트 조회")
    void findAllByIdIn() {
    
        //given
        Genre genre1 = Genre.builder()
                .name("genre1")
                .build();
        genreRepository.save(genre1);

        Genre genre2 = Genre.builder()
                .name("genre2")
                .build();
        genreRepository.save(genre2);

        Genre genre3 = Genre.builder()
                .name("genre3")
                .build();
        genreRepository.save(genre3);

        List<Long> list = new ArrayList<>();
        list.add(genre2.getId());
        list.add(genre3.getId());

        //when
        List<Genre> find = genreRepository.findAllByIdIn(list);

        //then
        assertEquals(2, find.size());
    }
}
