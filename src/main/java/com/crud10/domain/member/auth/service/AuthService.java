package com.crud10.domain.member.auth.service;

import com.crud10.common.JwtUtil;
import com.crud10.domain.member.auth.dto.LoginRequest;
import com.crud10.domain.member.auth.dto.LoginResponse;
import com.crud10.domain.member.auth.dto.SingupRequest;
import com.crud10.domain.member.auth.dto.SingupMemberResponse;
import com.crud10.domain.member.dto.ApiRespose;
import com.crud10.domain.member.entity.Member;
import com.crud10.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.crud10.common.Role.MEMBER;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public SingupMemberResponse signup(SingupRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용중인 이메일");
        }

        Member member = new Member(
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()), //비밀번호 암호화
                MEMBER
        );

        Member save = memberRepository.save(member);

        return SingupMemberResponse.of(save);
    }


    @Transactional
    public LoginResponse login(LoginRequest request) {

        //이메일 검증
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일이 없습니다."));

        //비밀번호 검증
        if(!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
        }

        //토큰 발급
        String token = jwtUtil.generateToken(member);
        return new LoginResponse(token);

    }
}
