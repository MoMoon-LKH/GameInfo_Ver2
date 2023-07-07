package com.gmi.gameInfo.config;

import com.gmi.gameInfo.jwt.JwtAuthenticationEntryPoint;
import com.gmi.gameInfo.jwt.JwtSecurityConfig;
import com.gmi.gameInfo.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final TokenProvider tokenProvider;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // http Security
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .cors().configurationSource(corsConfigurationSource())

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)

                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeHttpRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/api/email/**").permitAll()
                .antMatchers("/api/members/register", "/api/members/duplicate-loginId").permitAll()
                .antMatchers("/api/auth/login", "/api/auth/reissue-token").permitAll()
                .antMatchers(HttpMethod.GET, "/api/post/{id}", "/api/post/list/{categoryId}", "/api/news/list/{platformId}", "/api/news/{id}").permitAll()
                .antMatchers("/api/docs", "/v2/api-docs").permitAll()
                .antMatchers(HttpMethod.GET, "/api/comment/news/{newsId}", "/api/comment/post/{postId}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/main").permitAll()
                .antMatchers("/api/auth/logout").hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/image/upload").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/api/game").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/game/{id}").hasRole("ADMIN")
                .anyRequest().authenticated()

                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

       return http.build();
    }


    // web Security
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return web -> {
            web.ignoring().antMatchers(
                    "/swagger-ui/**",
                    "/api/docs",
                    "/swagger-resources/**",
                    "/webjars/**",
                    "/favicon.ico");
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOrigin("https://gameinfo.momoon.kro.kr");
        config.addAllowedOrigin("http://gameinfo.momoon.kro.kr");
        config.addAllowedOrigin("https://www.gameinfo.momoon.kro.kr");
        config.addAllowedOrigin("http://www.gameinfo.momoon.kro.kr");
        config.addAllowedOrigin("gameinfo.momoon.kro.kr");
        config.addAllowedOrigin("www.gameinfo.momoon.kro.kr");
        config.addAllowedOrigin("http://gameinfo-front:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }



}
