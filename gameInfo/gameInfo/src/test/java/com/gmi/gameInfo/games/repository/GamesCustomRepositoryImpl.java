package com.gmi.gameInfo.games.repository;

import com.gmi.gameInfo.games.domain.Games;
import com.gmi.gameInfo.games.domain.QGames;
import com.gmi.gameInfo.games.domain.QGamesGenre;
import com.gmi.gameInfo.games.domain.QGamesPlatform;
import com.gmi.gameInfo.games.domain.dto.GamesFindDto;
import com.gmi.gameInfo.genre.domain.QGenre;
import com.gmi.gameInfo.platform.domain.QPlatform;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GamesCustomRepositoryImpl implements GamesCustomRepository{

    private final JPAQueryFactory factory;

    private QGames games = QGames.games;

    private QGamesPlatform gamesPlatform = QGamesPlatform.gamesPlatform;

    private QGamesGenre gamesGenre = QGamesGenre.gamesGenre;

    private QPlatform platform = QPlatform.platform;

    private QGenre genre = QGenre.genre;

    @Override
    public List<Games> findByPageable(GamesFindDto dto, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        if (dto.getSearch() != null) {
            builder.and(games.name.like("%" + dto.getSearch() + "%"));
        }

        if (dto.getPlatformIds() != null && dto.getPlatformIds().size() > 0) {
            builder.and(platform.id.in(dto.getGenreIds()));
        }

        if (dto.getGenreIds() != null && dto.getGenreIds().size() > 0) {
            builder.and(genre.id.in(dto.getGenreIds()));
        }


        List<Long> ids = factory.select(
                        games.id
                ).from(games)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return factory.select(
                        games
                ).from(games)
                .leftJoin(games.platforms, gamesPlatform).fetchJoin()
                .leftJoin(games.genres, gamesGenre).fetchJoin()
                .leftJoin(gamesPlatform.platform, platform).fetchJoin()
                .leftJoin(gamesGenre.genre, genre).fetchJoin()
                .where(games.id.in(ids))
                .orderBy(games.name.asc())
                .fetch();
    }

    @Override
    public Optional<Games> findOneDetailById(Long id) {

        return Optional.ofNullable(
                factory.select(
                                games
                        )
                        .from(games)
                        .leftJoin(games.platforms, gamesPlatform).fetchJoin()
                        .leftJoin(games.genres, gamesGenre).fetchJoin()
                        .leftJoin(gamesPlatform.platform, platform).fetchJoin()
                        .leftJoin(gamesGenre.genre, genre).fetchJoin()
                        .where(games.id.eq(id))
                        .fetchOne()
        );
    }
}
