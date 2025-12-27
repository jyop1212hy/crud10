package com.crud10.domain.member.dto;

import com.crud10.domain.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FindMemberResponse {


    private final Long id;
    private final String name;


    public static FindMemberResponse of(Member member) {
        return new FindMemberResponse(member.getId(),
                member.getName()
        );
    }

}

