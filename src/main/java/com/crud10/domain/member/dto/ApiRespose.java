package com.crud10.domain.member.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiRespose<T> {

    private final String massage;
    private final int status;
    private final T data;


    public static <T> ApiRespose<T> success(String massage, int status, T data) {
        return new ApiRespose<>(massage, status, data);
    }

    public static <T> ApiRespose<T> error(String massage, int status) {
        return new ApiRespose<>(massage, status, null);
    }
}
