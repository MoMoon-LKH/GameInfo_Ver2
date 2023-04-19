package com.gmi.gameInfo.news.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NewsCustomRepositoryImpl implements NewsCustomRepository{

    private final JPAQueryFactory factory;

}
