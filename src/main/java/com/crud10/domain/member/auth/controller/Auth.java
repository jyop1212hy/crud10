package com.crud10.domain.member.auth.controller;

import com.crud10.domain.member.auth.dto.LoginRequest;
import com.crud10.domain.member.auth.dto.LoginResponse;
import com.crud10.domain.member.auth.dto.SingupRequest;
import com.crud10.domain.member.auth.dto.SingupMemberResponse;
import com.crud10.domain.member.auth.service.AuthService;
import com.crud10.domain.member.dto.ApiRespose;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class Auth {

    private final AuthService authService;

    public Auth(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiRespose<SingupMemberResponse>> signup(@RequestBody SingupRequest request) {
        ApiRespose<SingupMemberResponse> apiRespose = ApiRespose.success("성공", 200, authService.signup(request));
        return ResponseEntity.ok(apiRespose);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}