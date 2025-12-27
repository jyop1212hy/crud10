package com.crud10.domain.member.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SingupRequest {

    private final String name;
    private final String email;
    private final String password;

}
