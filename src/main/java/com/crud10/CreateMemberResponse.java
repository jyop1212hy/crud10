package com.crud10;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateMemberResponse {

    private final Long id;
    private final String name;


    public static CreateMemberResponse of(Member member) {
        return new CreateMemberResponse(member.getId(),
                member.getName()
        );
    }

}

