package com.gmi.gameInfo.news.repository;


import com.gmi.gameInfo.news.domain.QNews;
import com.gmi.gameInfo.news.domain.dto.NewsListDto;
import com.gmi.gameInfo.news.domain.dto.NewsSearchDto;
import com.gmi.gameInfo.platform.domain.QPlatform;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NewsCustomRepositoryImpl implements NewsCustomRepository{

    private final JPAQueryFactory factory;

    private QNews news = QNews.news;

    private QPlatform platform = QPlatform.platform;


    @Override
    public List<NewsListDto> findListByPageable(NewsSearchDto searchDto, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

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
                        news.content,
                        news.member.id.as("memberId"),
                        news.member.nickname.as("nickname"),
                        news.createDate
                        ))
                .from(news)
                .leftJoin(news.platform, platform)
                .where(builder)
                .orderBy(news.createDate.desc())
                .fetch();
    }
}
