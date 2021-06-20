package com.taes.board.api.config;


import com.taes.board.api.config.security.AuthenticationFailHandler;
import com.taes.board.api.config.security.AuthorizationFailHandler;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Order(1)
@TestConfiguration
public class TestSecurityConfig extends WebSecurityConfigurerAdapter
{
    private final AuthenticationFailHandler authenticationFailHandler;
    private final AuthorizationFailHandler authorizationFailHandler;

    public TestSecurityConfig(
        AuthenticationFailHandler authenticationFailHandler
        , AuthorizationFailHandler authorizationFailHandler)
    {
        this.authenticationFailHandler = authenticationFailHandler;
        this.authorizationFailHandler = authorizationFailHandler;
    }

    /**
     * H2 iframe 접근을 위한 설정 추가
     * security 정보 session 전달을 위한 session 사용 설정
     *
     * @param http
     * @throws Exception
     */
    @Primary
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
            .csrf().disable()
            .headers()
                .frameOptions().sameOrigin()
                .and()
            .exceptionHandling()
                .authenticationEntryPoint(authenticationFailHandler)
                .accessDeniedHandler(authorizationFailHandler)
                .and()
            .authorizeRequests()
                .anyRequest().permitAll()
                .and()
            .oauth2ResourceServer()
                .jwt().jwtAuthenticationConverter(getJwtAuthenticationConverter());
    }

    private Converter<Jwt, AbstractAuthenticationToken> getJwtAuthenticationConverter()
    {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt ->
        {
            @SuppressWarnings("unchecked")
            List<String> auths = (List<String>) jwt.getClaims().get("authorities");
            return CollectionUtils.isEmpty(auths)
                ? new ArrayList<>()
                : auths.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        });
        return converter;
    }
}
