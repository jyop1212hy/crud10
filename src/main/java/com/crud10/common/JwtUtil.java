package com.crud10.common;

import com.crud10.domain.member.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.rmi.ServerException;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    /**
     * 기본셋팅
     */
    // Authorization 헤더에서 쓰는 접두어
    public static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_EXPIRE_MS = 60 * 60 * 1000L; // 토큰 유효 시간 60분 선언

    private SecretKey key;  // ✅ Base64 디코딩된 secret을 이용해 만든 실제 서명 키 객체
    private JwtParser parser;   // ✅ 토큰 검증/파싱에 쓸 parser(서명 검증 포함)

    // ✅ application.yml(or properties)에서 jwt.secret.key 값을 주입받음
    // ⚠️ 이 값은 "Base64로 인코딩된 문자열"이어야 함(아래에서 디코딩함)
    @Value("${jwt.secret.key}")
    private String secretKeyString;

    /**
     * ✅ 스프링이 JwtUtil 빈을 만든 직후 딱 1번 실행됨
     * - secretKeyString(Base64 문자열) -> byte[]로 디코딩
     * - byte[] -> HMAC-SHA SecretKey로 변환
     * - 서명 검증 가능한 JwtParser 생성
     */
    @PostConstruct
    public void init() {
        byte[] bytes = Decoders.BASE64.decode(secretKeyString);     // ✅ Base64 문자열을 byte[]로 디코딩
        this.key = Keys.hmacShaKeyFor(bytes);   // ✅ HMAC-SHA256/384/512 용 SecretKey 생성(길이가 충분해야 함)
        this.parser = Jwts.parser()     // ✅ parser 생성: 이 parser는 verifyWith(key)로 "서명 검증"까지 수행 가능
                .verifyWith(this.key)
                .build();
    }

    /**
     * ✅ 토큰 생성(발급)
     * - username을 claim으로 넣고
     * - 발급시간(iat), 만료시간(exp) 설정하고
     * - signWith(key)로 서명한 뒤
     * - compact()로 최종 JWT 문자열 완성
     */
    public String generateToken(Member member) {
        Date now = new Date();                                              // ✅ 현재 시각(발급 시각)
        return BEARER_PREFIX + Jwts.builder()
                .claim("memberId", member.getId())// ✅ 최종적으로 "Bearer " + JWT문자열 형태로 리턴 (헤더에 바로 넣기 편함)
                .claim("memberName", member.getName())                          // ✅ 페이로드(Claims)에 username 저장(나중에 꺼내서 누구인지 판단)
                .claim("email", member.getEmail())
                .claim("role",member.getRole())
                .issuedAt(now)                                              // ✅ 토큰 발급 시간
                .expiration(new Date(now.getTime() + TOKEN_EXPIRE_MS))      // ✅ 토큰 만료 시간
                .signWith(this.key, Jwts.SIG.HS256)                         // ✅ 서명(위변조 방지)
                .compact();                                                 // ✅ JWT 문자열로 직렬화(완성)
    }

    /**
     * ✅ Authorization 헤더에서 "Bearer "를 떼고 순수 토큰만 추출
     * - 헤더가 없거나 형식이 잘못이면 null 반환
     */
    //방법 1
    public String substringToken(String tokenValue) throws ServerException {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        throw new ServerException("Not Found Token");
    }

    //방법 2
//    public String extractToken(String authorizationHeader) {
//        if (authorizationHeader == null) return null;   // ✅ 헤더 자체가 없으면 토큰도 없음
//        if (!authorizationHeader.startsWith(BEARER_PREFIX)) return null;        // ✅ "Bearer "로 시작하지 않으면 우리가 원하는 형식이 아님
//        return authorizationHeader.substring(BEARER_PREFIX.length());       // ✅ "Bearer " 이후의 순수 JWT 문자열만 잘라서 반환
//    }


    /**
     * ✅ 토큰 검증
     * - 서명 검증 + 만료 검증 포함
     * - 성공하면 true, 실패하면 false
     */
    public boolean validateToken(String token) {
        if (token == null || token.isBlank()) return false;
        try {
            // ✅ parseSignedClaims()가 성공하면:
            //    1) 서명이 맞고
            //    2) 만료(exp) 안됐고
            //    3) 구조가 정상이라는 뜻
            parser.parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("Invalid JWT: {}", e.toString());
            // ✅ 서명 틀림, 만료됨, 형식 이상 등 모든 실패 케이스
            return false;
        }
    }


    /**
     * 토큰 파싱
     */
    public Claims extractAllClaims(String token) {
        return parser.parseSignedClaims(token).getPayload();
    }

    //아이디
    public Long extractMemberId(String token) {
        return extractAllClaims(token).get("memberId", Long.class);
    }

    //이름
    public String extractMemberName(String token) {
        return extractAllClaims(token).get("memberName", String.class);
    }

    //이메일
    public String extractEmail(String token) {
        return extractAllClaims(token).get("email", String.class);
    }

}
