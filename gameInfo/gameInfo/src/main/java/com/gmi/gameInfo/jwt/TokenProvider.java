package com.gmi.gameInfo.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;


@Component
public class TokenProvider implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private final long tokenValidityInSecond;
    private final int refreshValidityDate;

    private Key key;
    private Key refreshKey;

    public TokenProvider(
        @Value("${jwt.token-validity-in-second}") long tokenValidityInSecond,
        @Value("${jwt.refresh-validity-date}") int refreshValidityDate
    ) {
        this.tokenValidityInSecond = tokenValidityInSecond * 1000;
        this.refreshValidityDate = refreshValidityDate * 60 * 60 * 24 * 1000;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        this.refreshKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String createAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));

        long now = (new Date()).getTime();
        Date validDate = new Date(now + this.tokenValidityInSecond);

        return Jwts.builder()
                .setSubject("gameInfo")
                .claim("username", authentication.getName())
                .claim("auth", authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validDate)
                .compact();
    }

    public String createRefreshToken(Authentication authentication, int maxAge) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));

        Calendar cal = Calendar.getInstance();

        if(maxAge > 0) {
            cal.add(Calendar.DATE, maxAge * 1000);
        } else {
            cal.add(Calendar.DATE, refreshValidityDate);
        }
        Date validDate = new Date(cal.getTimeInMillis());

        return Jwts.builder()
                .setSubject("gameinfo_refresh")
                .claim("username", authentication.getName())
                .claim("auth", authorities)
                .signWith(refreshKey, SignatureAlgorithm.HS512)
                .setExpiration(validDate)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build().parseClaimsJws(token)
                .getBody();

        Collection<GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User user = new User(claims.get("username").toString(), "", authorities);

        return new UsernamePasswordAuthenticationToken(user, token, authorities);
    }

    public Authentication getRefreshAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(refreshKey)
                .build().parseClaimsJws(token)
                .getBody();

        Collection<GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User user = new User(claims.get("username").toString(), "", authorities);

        return new UsernamePasswordAuthenticationToken(user, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
        } catch (SecurityException | MalformedJwtException e){
            logger.info("잘못된 JWT 서명입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원하지않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("잘못된 JWT 토큰입니다.");
        }

        return false;
    }
}
