package com.gmi.gameInfo.platform.domain;

import com.gmi.gameInfo.games.domain.GamesPlatform;
import com.gmi.gameInfo.news.domain.News;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Platform {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "platform_id")
    private Long id;

    @Column(length = 25)
    private String name;

    @OneToMany(mappedBy = "platform",fetch = FetchType.LAZY)
    private List<News> news;

    @OneToMany(mappedBy = "platform", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<GamesPlatform> gamesPlatforms = new ArrayList<>();
}
