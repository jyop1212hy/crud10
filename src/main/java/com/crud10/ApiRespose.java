package com.crud10;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiRespose<T> {

    private final String massage;
    private final int status;
    private final T data;


}
