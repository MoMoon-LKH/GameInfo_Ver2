package com.gmi.gameInfo.genre.domain;

import com.gmi.gameInfo.games.domain.GamesGenre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Genre {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private Long id;

    @Column(length = 25)
    private String name;


    @OneToMany(mappedBy = "genre", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GamesGenre> gamesGenres = new HashSet<>();

    public void associateGamesGenre(GamesGenre gamesGenre) {
        gamesGenres.add(gamesGenre);
    }


}
