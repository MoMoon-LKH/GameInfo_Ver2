package com.gmi.gameInfo.config;

import com.gmi.gameInfo.comment.domain.dto.CommentDto;
import com.gmi.gameInfo.exceptionHandler.ErrorResponse;
import com.gmi.gameInfo.image.domain.dto.ImageDto;
import com.gmi.gameInfo.member.domain.dto.LoginResponseDto;
import com.gmi.gameInfo.member.domain.dto.MemberDto;
import com.gmi.gameInfo.news.domain.dto.NewsDto;
import com.gmi.gameInfo.post.domain.dto.PostListDto;
import com.gmi.gameInfo.post.domain.dto.PostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import com.fasterxml.classmate.TypeResolver;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.lettuce.core.internal.LettuceSets.newHashSet;
import static io.swagger.v3.oas.models.security.SecurityScheme.*;


@Configuration
public class SwaggerConfig {

    @Autowired
    private TypeResolver typeResolver;

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("게임정보 웹 API")
                .description("게임정보 웹 프로젝트 API 설명 문서입니다")
                .version("1.0")
                .contact(new Contact("이기호", "https://github.com/MoMoon-LKH", "rlgh28@gmail.com"))
                .build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)

                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(apiKeys())
                .protocols(newHashSet( "https"))
                .additionalModels(
                        typeResolver.resolve(ErrorResponse.class),
                        typeResolver.resolve(MemberDto.class),
                        typeResolver.resolve(PostVo.class),
                        typeResolver.resolve(LoginResponseDto.class),
                        typeResolver.resolve(ImageDto.class),
                        typeResolver.resolve(PostListDto.class),
                        typeResolver.resolve(NewsDto.class),
                        typeResolver.resolve(CommentDto.class)
                )
                .ignoredParameterTypes(AuthenticationPrincipal.class);
    }

    private List<SecurityScheme> apiKeys() {
        List<SecurityScheme> list = new ArrayList<>();
        list.add(new ApiKey("JWT", "Authorization", "header"));

        return list;
    }


    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global","accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }
}
