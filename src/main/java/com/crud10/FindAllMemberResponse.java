package com.crud10;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class FindAllMemberResponse {

    private final List<Member> all;
}
