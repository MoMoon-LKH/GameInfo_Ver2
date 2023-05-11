package com.gmi.gameInfo.news.repository;


import com.gmi.gameInfo.likes.domain.LikeType;
import com.gmi.gameInfo.likes.domain.QNewsLikes;
import com.gmi.gameInfo.news.domain.QNews;
import com.gmi.gameInfo.news.domain.dto.NewsDto;
import com.gmi.gameInfo.news.domain.dto.NewsListDto;
import com.gmi.gameInfo.news.domain.dto.NewsSearchDto;
import com.gmi.gameInfo.platform.domain.QPlatform;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NewsCustomRepositoryImpl implements NewsCustomRepository{

    private final JPAQueryFactory factory;

    private QNews news = QNews.news;

    private QPlatform platform = QPlatform.platform;

    private QNewsLikes likes = QNewsLikes.newsLikes;


    @Override
    public List<NewsListDto> findListByPageable(NewsSearchDto searchDto, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        int commentCnt = 0;
        Long platformId = searchDto.getPlatformId();
        String searchWord = searchDto.getSearchWord();

        StringExpression resultTitle = Expressions.stringTemplate("'[' || {0} || '] ' || {1}", news.platform.name, news.title);


        if (platformId != null && platformId != 0) {
            builder.and(
                    news.platform.id.eq(platformId)
            );
        }

        if (searchWord != null && !searchWord.isEmpty()) {
            String select = searchDto.getSearchSelect();

            if (select.equals("writer")) {
                builder.and(
                        news.member.nickname.containsIgnoreCase(searchWord)
                );
            } else {
                builder.and(
                        news.title.containsIgnoreCase(searchWord)
                );
            }
        }


        return factory
                .select(Projections.bean(NewsListDto.class,
                        news.id,
                        resultTitle.as("title"),
                        news.member.id.as("memberId"),
                        news.member.nickname.as("nickname"),
                        news.createDate,
                        news.comments.size().as("commentCount"),
                        news.views.as("views"),
                        ExpressionUtils.as(
                                JPAExpressions.select(
                                        likes.count().castToNum(Integer.class)
                                )
                                .from(likes)
                                .where(likes.news.id.eq(news.id).and(likes.likeType.eq(LikeType.LIKE)))


                                        , "likesCount"
                        )
                        ))
                .from(news)
                .leftJoin(news.platform, platform)
                .where(builder)
                .orderBy(news.createDate.desc())
                .fetch();
    }

    @Override
    public Optional<NewsDto> findDtoOneById(Long id) {

        return Optional.ofNullable(
                factory.select(
                        Projections.bean(NewsDto.class,
                                news.id,
                                news.title,
                                news.content,
                                news.createDate
                        ))
                .from(news)
                .where(news.id.eq(id))
                .fetchOne());
    }
}
