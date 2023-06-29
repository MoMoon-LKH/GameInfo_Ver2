package com.gmi.gameInfo.news.repository;


import com.gmi.gameInfo.common.date.CommonDateFunction;
import com.gmi.gameInfo.image.domain.QImages;
import com.gmi.gameInfo.likes.domain.LikeType;
import com.gmi.gameInfo.likes.domain.QNewsLikes;
import com.gmi.gameInfo.main.dto.NewsImageListDto;
import com.gmi.gameInfo.main.dto.NewsSimpleDto;
import com.gmi.gameInfo.news.domain.QNews;
import com.gmi.gameInfo.news.domain.dto.NewsDto;
import com.gmi.gameInfo.news.domain.dto.NewsListDto;
import com.gmi.gameInfo.news.domain.dto.NewsSearchDto;
import com.gmi.gameInfo.platform.domain.QPlatform;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NewsCustomRepositoryImpl implements NewsCustomRepository{

    private final JPAQueryFactory factory;

    private final CommonDateFunction dateFunction = new CommonDateFunction();

    private QNews news = QNews.news;

    private QPlatform platform = QPlatform.platform;

    private QNewsLikes likes = QNewsLikes.newsLikes;

    private QImages image = QImages.images;


    @Override
    public List<NewsListDto> findListByPageable(NewsSearchDto searchDto, Pageable pageable) throws ParseException {


        BooleanBuilder builder = new BooleanBuilder();
        int commentCnt = 0;
        Long platformId = searchDto.getPlatformId();
        String searchWord = searchDto.getSearchWord();
        Date today = getToday();

        StringExpression resultTitle = Expressions.stringTemplate("'[' || {0} || '] ' || {1}", news.platform.name, news.title);

        builder.and(news.deleteYn.eq(false));

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
                        new CaseBuilder()
                                .when(news.createDate.before(today)).then(news.createDate)
                                        .otherwise((Date) null).as("createDate"),
                        news.createDate.as("createHour"),
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
                .where(news.id.eq(id).and(news.deleteYn.eq(false)))
                .fetchOne());
    }

    @Override
    public List<NewsImageListDto> findNewsImageListAtMain() throws ParseException {

        StringExpression imageName = Expressions.stringTemplate("{0} || {1}", image.fileName, image.extension);

        List<NewsImageListDto> images = factory.select(
                        Projections.bean(NewsImageListDto.class,
                                news.id.as("id"),
                                news.title.as("title"),
                                news.title.as("imageName")
                                ))
                .from(news)
                .where(
                        news.deleteYn.eq(false)
                )
                .innerJoin(news.images, image)
                .offset(0)
                .orderBy(news.likes.size().desc(), news.createDate.desc())
                .limit(5)
                .fetch();

        for (NewsImageListDto dto : images) {

            dto.setImageName(
                    factory.select(
                                    imageName
                            ).from(image)
                            .join(image.news, news)
                            .where(image.news.id.eq(dto.getId()))
                            .orderBy(image.id.asc())
                            .fetchFirst()
            );
        }
        return images;
    }

    @Override
    public List<NewsSimpleDto> findNewsListByNotIds(List<Long> ids) {

        StringExpression title = Expressions
                .stringTemplate("'[' || {0} || '] ' || {1}", news.platform.name, news.title);

        return factory.select(
                        Projections.bean(NewsSimpleDto.class,
                                news.id.as("id"),
                                title.as("title")
                        )
                )
                .from(news)
                .where(news.id.notIn(ids).and(news.deleteYn.eq(false)))
                .orderBy(news.createDate.desc())
                .offset(0)
                .limit(8)
                .fetch();
    }


    private Date getToday() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new Date());

        return format.parse(date);
    }
}
