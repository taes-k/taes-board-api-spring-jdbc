package com.taes.board.api.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class QueryDslConfig
{
    @Bean
    public JPAQueryFactory wmpJpaQueryFactory(EntityManager entityManager)
    {
        return new JPAQueryFactory(entityManager);
    }
}
