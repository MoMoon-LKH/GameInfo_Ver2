package com.gmi.gameInfo.games.domain;

import com.gmi.gameInfo.genre.domain.Genre;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GamesGenre {

    @EmbeddedId
    private GamesGenreId id;

    @MapsId("gamesId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "games_id")
    private Games games;

    @MapsId("genreId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    public GamesGenre(Games games, Genre genre) {
        this.games = games;
        this.genre = genre;
        this.id = new GamesGenreId(games.getId(), genre.getId());
    }


}
