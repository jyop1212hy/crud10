package com.crud10.common;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if (!StringUtils.hasText(authorization) || !authorization.startsWith(JwtUtil.BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token;
        try {
            token = jwtUtil.substringToken(authorization); // "Bearer " 제거된 순수 JWT
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!jwtUtil.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ 토큰에서 사용자 정보 꺼내기
        Long memberId = jwtUtil.extractMemberId(token);
        String memberName = jwtUtil.extractMemberName(token);
        String email = jwtUtil.extractEmail(token);

        // ✅ 인증객체 만들기 (권한 없으면 빈 리스트로 둬도 됨)
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        // principal에 뭘 넣을지 선택:
        // - memberId만 넣어도 되고
        // - DTO 만들어서 memberId/memberName/email 넣어도 됨
        var auth = new UsernamePasswordAuthenticationToken(memberId, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }
}