package com.gmi.gameInfo.games.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmi.gameInfo.games.domain.dto.GamesCreateDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Games {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private String explanation;

    private String mainImage;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDate createDate;

    @OneToMany(mappedBy = "games", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GamesGenre> genres = new ArrayList<>();

    @OneToMany(mappedBy = "games", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GamesPlatform> platforms = new ArrayList<>();

    public Games(GamesCreateDto dto) {
        this.name = dto.getName();
        this.explanation = dto.getExplanation();
        this.mainImage = dto.getMainImage();
    }

    public static Games createGames(GamesCreateDto dto) {
        return new Games(dto);
    }
}
