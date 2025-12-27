package com.crud10.domain.member.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateAllMemberResponse {

    private final Long id;
    private final String name;

}
