package com.taes.board.api.config.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationFailHandler implements AuthenticationEntryPoint
{
    @Override
    public void commence(
        HttpServletRequest request
        , HttpServletResponse response
        , AuthenticationException authException) throws IOException
    {
        if (authException != null && StringUtils.isNotEmpty(authException.getMessage()))
            response.sendError(HttpStatus.UNAUTHORIZED.value(), authException.getMessage());
        else
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "UnAuthenticated");
    }
}