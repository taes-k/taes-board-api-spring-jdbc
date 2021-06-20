package com.taes.board.api.config.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthorizationFailHandler implements AccessDeniedHandler
{
    @Override
    public void handle(
        HttpServletRequest request
        , HttpServletResponse response
        , AccessDeniedException accessDeniedException)
        throws IOException
    {
        if (accessDeniedException != null && StringUtils.isNotEmpty(accessDeniedException.getMessage()))
            response.sendError(HttpStatus.FORBIDDEN.value(), accessDeniedException.getMessage());
        else
            response.sendError(HttpStatus.FORBIDDEN.value(), "UnAuthorized");
    }
}