package com.example.study_monster_back.jwt;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider implements InitializingBean {
    private final String AUTHORITIES_KEY = "auth";
    private String secretKey;
    private long tokenValidityInMilliseconds;
    private Key key;

    private final UserDetailsService userDetailsService;

    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.token-validity-in-seconds}") Long tokenValidityInMilliseconds,
            UserDetailsService userDetailsService) {
        this.secretKey = secretKey;
        this.tokenValidityInMilliseconds = tokenValidityInMilliseconds * 1000;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Authentication authentication) {
        // 권한 값을 받아 하나의 문자열로 합침
        String authorities = authentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.joining(","));

        long now = new Date().getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(validity)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<? extends SimpleGrantedAuthority> authorities = Arrays
                .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        org.springframework.security.core.userdetails.User principal = new org.springframework.security.core.userdetails.User(
                claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public String extractEmail(String token) {
        try {
            // 토큰 파싱 및 클레임 추출
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            // Subject 클레임에서 이메일 추출
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰에서 이메일 추출 시도: {}", e.getMessage());
            throw e;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException | UnsupportedJwtException
                | IllegalArgumentException e) {
            log.error("JWT 토큰에서 이메일 추출 중 오류 발생: {}", e.getMessage(), e);
            throw e;
        }
    }
    // 만료시간 추출출
    public Date getExpirationToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration();
    }
}
