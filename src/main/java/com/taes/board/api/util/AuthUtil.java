package com.taes.board.api.util;

import com.taes.board.api.config.security.UserInformation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Log4j2
public class AuthUtil
{
    private AuthUtil()
    {
        throw new IllegalStateException("Utility class");
    }

    private static Authentication getAuthentication()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || (!authentication.isAuthenticated()))
            throw new IllegalStateException("notAuthenticated");

        log.debug("Authentication : {}", authentication.toString());

        return authentication;
    }

    private static Map<String, Object> getPrincipalClaims()
    {
        Authentication authentication = getAuthentication();
        return ((Jwt) authentication.getPrincipal()).getClaims();
    }

    private static UserInformation getCurrentUser()
    {
        return new UserInformation(getPrincipalClaims());
    }

    public static String getUserId()
    {
        String userId = getCurrentUser().getUsername();
        if (StringUtils.isEmpty(userId))
            throw new IllegalArgumentException("userId is empty");

        return userId;
    }

    @SuppressWarnings("unchecked")
    public static List<String> getUserAuthorities()
    {
        List<String> authorities = (List<String>) getPrincipalClaims().get("authorities");
        if (CollectionUtils.isEmpty(authorities))
            return new ArrayList<>();

        return authorities;
    }
}
