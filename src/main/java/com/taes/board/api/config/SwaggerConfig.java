package com.taes.board.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

import static java.util.Collections.singletonList;

@Configuration
public class SwaggerConfig
{
    private static final String SECURITY_REFERENCE_ID = "jwt";

    @Bean
    public Docket api()
    {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.taes.board"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo())
            .securityContexts(singletonList(securityContext()))
            .securitySchemes(singletonList(apiKey()));
    }

    private ApiInfo apiInfo()
    {
        return new ApiInfoBuilder()
            .title("TAES-BOARD API")
            .description("taes-board-api API")
            .version("0.0.1")
            .termsOfServiceUrl("terms of controller url")
            .license("all rights reserved taes-k")
            .licenseUrl("http://github.com/taes-k")
            .build();
    }

    private List<SecurityReference> defaultAuth()
    {
        AuthorizationScope authorizationScope
            = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;

        return singletonList(new SecurityReference(SECURITY_REFERENCE_ID, authorizationScopes));
    }

    private SecurityContext securityContext()
    {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .forPaths(PathSelectors.any())
            .build();
    }

    private ApiKey apiKey()
    {
        return new ApiKey(SECURITY_REFERENCE_ID, HttpHeaders.AUTHORIZATION, "header");
    }
}
