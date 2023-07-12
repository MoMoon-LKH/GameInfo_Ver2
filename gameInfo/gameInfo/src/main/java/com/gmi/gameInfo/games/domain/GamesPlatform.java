package com.gmi.gameInfo.games.domain;

import com.gmi.gameInfo.platform.domain.Platform;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class GamesPlatform {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "games_id")
    private Games games;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_id")
    private Platform platform;

    public GamesPlatform(Games games, Platform platform) {
        this.games = games;
        this.platform = platform;
    }
}
