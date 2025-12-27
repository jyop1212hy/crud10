package com.crud10.domain.member.auth.dto;

import com.crud10.domain.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SingupMemberResponse {

    private final Long id;
    private final String name;
    private final String email;

    public static SingupMemberResponse of(Member member) {
        return new SingupMemberResponse(
                member.getId(),
                member.getName(),
                member.getEmail()
        );
    }

}

