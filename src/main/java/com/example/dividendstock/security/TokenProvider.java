package com.example.dividendstock.security;

import com.example.dividendstock.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1 hour
    private static final String KEY_ROLES = "roles";

    private final MemberService memberService;

    @Value("${spring.jwt.secret}")
    private  String secretKey;

    private Key getSigninKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }   //signWith 문제 해결 바이트 형식으로
    /**
     * 토큰 생성(발급)
     */
    public String generateToken(String username, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);     // 사용자 권한 정보 저장.
        claims.put(KEY_ROLES, roles);   // 키-벨류 타입으로 저장

        var now = new Date();
        var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);
//        return Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(now)               // 토큰 생성 시간
//                .setExpiration(expiredDate)     // 토큰 만료 시간
//                .signWith(SignatureAlgorithm.HS512, this.secretKey) // 사용할 알고리즘, 비밀키 - 시그니처 부분
//                .compact();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)               // 토큰 생성 시간
                .setExpiration(expiredDate)     // 토큰 만료 시간
                .signWith(getSigninKey(secretKey), SignatureAlgorithm.HS512)
                .compact();
    }

    //jwt 로부터 인증 정보를 가저오는 메서트
    public Authentication getAuthentication(String jwt) {
        UserDetails userDetails = this.memberService.loadUserByUsername(this.getUsername(jwt));

        // 사용자 정보와 사용자 권한정보를 포함하는 값 리턴
        return new UsernamePasswordAuthenticationToken(userDetails,""
                                                , userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return this.parseClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        if(!StringUtils.hasText(token)) return false;

        Claims claims = this.parseClaims(token);
        return !claims.getExpiration().before(new Date()); // 만료 시간이 현재보다 이전인지 아닌지 체크
    }
    private Claims parseClaims(String token) {  // 토큰이 유효한지 확인하는 메서드(외부 사용 X -> private)

        try {
//            return Jwts.parser()
//                    .setSigningKey(this.secretKey)
//                    .parseClaimsJws(token)
//                    .getBody();
            return Jwts.parserBuilder()
                    .setSigningKey(this.secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {       // 만료 시간 이후 파싱하면 오류남
            return e.getClaims();
        }

    }
}
