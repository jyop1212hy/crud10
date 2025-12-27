package com.crud10.domain.member.dto;

import com.crud10.domain.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class FindAllMemberResponse {

    private final List<Member> all;
}
