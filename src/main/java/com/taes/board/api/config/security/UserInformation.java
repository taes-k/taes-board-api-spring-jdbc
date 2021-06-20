package com.taes.board.api.config.security;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Getter
@Setter
public class UserInformation
{
    private String username;
    private List<GrantedAuthority> authorities;

    public UserInformation(Map<String, Object> userMap)
    {
        if (CollectionUtils.isEmpty(userMap))
            throw new IllegalArgumentException("userMap is null");

        this.username = (String) userMap.get("user_name");
        if (StringUtils.isEmpty(username))
            throw new IllegalArgumentException("user_name is empty");

        @SuppressWarnings("unchecked")
        List<String> auths = (List<String>) userMap.get("authorities");
        this.authorities = CollectionUtils.isEmpty(auths)
            ? new ArrayList<>()
            : auths.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
