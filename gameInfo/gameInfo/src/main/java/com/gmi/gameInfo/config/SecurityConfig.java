package com.gmi.gameInfo.config;

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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final TokenProvider tokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // http Security
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeHttpRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/api/email/**").permitAll()
                .antMatchers("/api/members/register", "/api/members/duplicate-loginId").permitAll()
                .antMatchers("/api/auth/login").permitAll()
                .antMatchers(HttpMethod.GET, "/api/post/{id}").permitAll()
                .antMatchers("/api/docs", "/v2/api-docs").permitAll()
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
                    "/webjars/**");
        };
    }




}
