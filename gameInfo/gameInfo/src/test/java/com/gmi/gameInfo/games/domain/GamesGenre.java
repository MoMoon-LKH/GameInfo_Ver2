package com.gmi.gameInfo.games.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmi.gameInfo.genre.domain.Genre;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
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

}
