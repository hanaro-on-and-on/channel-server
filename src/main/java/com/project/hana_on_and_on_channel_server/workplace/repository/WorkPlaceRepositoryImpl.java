package com.project.hana_on_and_on_channel_server.workplace.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WorkPlaceRepositoryImpl implements WorkPlaceRepositoryCustom {

    private final JPAQueryFactory queryFactory;
}
