package com.gmi.gameInfo.games.domain;

import com.gmi.gameInfo.platform.domain.Platform;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class GamesPlatform {

    @EmbeddedId
    private GamesPlatformId id;

    @MapsId("gamesId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "games_id")
    private Games games;

    @MapsId("platformId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_id")
    private Platform platform;
}
