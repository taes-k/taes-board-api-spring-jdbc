package com.taes.board.api.config;

import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrincipalMockMvcRequestBuilders
{
    public static MockHttpServletRequestBuilder get(String url)
    {
        return MockMvcRequestBuilders.get(url).session(getMockHttpSession());
    }

    public static MockHttpServletRequestBuilder post(String url)
    {
        return MockMvcRequestBuilders.post(url).session(getMockHttpSession());
    }

    public static MockHttpServletRequestBuilder delete(String url)
    {
        return MockMvcRequestBuilders.delete(url).session(getMockHttpSession());
    }

    public static MockHttpServletRequestBuilder put(String url)
    {
        return MockMvcRequestBuilders.put(url).session(getMockHttpSession());
    }

    private static MockHttpSession getMockHttpSession()
    {
        TestingAuthenticationToken principal = buildPrincipal();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            new SecurityContextImpl(principal));

        return session;
    }

    private static TestingAuthenticationToken buildPrincipal()
    {
        String username = "test-admin";
        List<String> authorities = Arrays.asList("USER", "ADMIN");

        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "RS256");
        headers.put("typ", "JWT");

        Map<String, Object> claims = new HashMap<>();
        claims.put("user_name", username);
        claims.put("authorities", authorities);

        Jwt jwt = new Jwt("test_token", null, java.time.Instant.MAX, headers, claims);
        return new TestingAuthenticationToken(jwt, authorities, "USER", "ADMIN");
    }
}
