package com.taes.board.api.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    private final AuthenticationFailHandler authenticationFailHandler;
    private final AuthorizationFailHandler authorizationFailHandler;

    public SecurityConfig(
        AuthenticationFailHandler authenticationFailHandler
        , AuthorizationFailHandler authorizationFailHandler)
    {
        this.authenticationFailHandler = authenticationFailHandler;
        this.authorizationFailHandler = authorizationFailHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
            .csrf().disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
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
