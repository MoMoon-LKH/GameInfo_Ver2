package com.gmi.gameInfo.games.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class GamesGenreId implements Serializable {

    @Column(name = "games_id")
    private Long gamesId;

    @Column(name = "genre_id")
    private Long genreId;

    public GamesGenreId(Long gamesId, Long genreId) {
        this.gamesId = gamesId;
        this.genreId = genreId;
    }
}
