package com.gmi.gameInfo.games.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmi.gameInfo.games.domain.dto.GamesCreateDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Games {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 35)
    private String name;

    @Lob
    private String explanation;

    private String mainImage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate releaseDate;

    @Column(columnDefinition = "boolean default false")
    private boolean deleteYn;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDate createDate;

    @OneToMany(mappedBy = "games", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GamesGenre> genres = new HashSet<>();

    @OneToMany(mappedBy = "games", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GamesPlatform> platforms = new HashSet<>();


    public Games(GamesCreateDto dto) {
        this.name = dto.getName();
        this.explanation = dto.getExplanation();
        this.mainImage = dto.getMainImage();
    }

    public void associatePlatform(GamesPlatform gamesPlatform) {
        platforms.add(gamesPlatform);
    }

    public void associateGenre(GamesGenre gamesGenre) {
        genres.add(gamesGenre);
    }

    public void updateDelete() {
        this.deleteYn = true;
    }

    public static Games createGames(GamesCreateDto dto) {
        return new Games(dto);
    }
}
