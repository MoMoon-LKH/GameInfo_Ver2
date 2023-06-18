package com.gmi.gameInfo.games.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class GamesPlatformId implements Serializable {

    @Column(name = "games_id")
    private Long gamesId;

    @Column(name = "platform_id")
    private Long platformId;

    public GamesPlatformId(Long gamesId, Long platformId) {
        this.gamesId = gamesId;
        this.platformId = platformId;
    }
}
