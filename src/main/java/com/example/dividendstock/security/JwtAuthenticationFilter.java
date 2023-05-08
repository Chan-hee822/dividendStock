package com.example.dividendstock.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String TOKEN_HEADER = "Authorization";
        // 토큰같은 경우 http 프로토콜 헤더에 포함되어있음, 어떤 키 키준으로 토큰을 주고 받을지 대한 키 설정
    public static final String TOKEN_PREFIX = "Bearer ";
        // 인증 타입을 나타낼 때 사용

    private final TokenProvider tokenProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request
                                    , HttpServletResponse response
                                    , FilterChain filterChain)
                                    throws ServletException, IOException {

        // request 에 토큰이 있는지 확인 -> 토큰 유효한지 확인
        String token = this.resolveTokenFromRequest(request);

        if (StringUtils.hasText(token) && this.tokenProvider.validateToken(token)) {
            // 토큰 유혀성 검증.
            Authentication auth = this.tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);

            log.info("[%s] -> %s", this.tokenProvider.getUsername(token), request.getRequestURI());
        }

        filterChain.doFilter(request, response);
    }

    //request 헤더로 부터 토큰을 꺼내옴
    private String resolveTokenFromRequest(HttpServletRequest request) {

        String token = request.getHeader(TOKEN_HEADER);     // 키에 해당하는 밸류를 뱉음

        if(!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)){
            return token.substring(TOKEN_PREFIX.length());
        }

        return null;
    }
}
