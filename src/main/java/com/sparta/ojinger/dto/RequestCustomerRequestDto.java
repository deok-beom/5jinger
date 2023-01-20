package com.sparta.ojinger.dto;

import lombok.Getter;

@Getter
public class RequestCustomerRequestDto {
    private final String message;

    public RequestCustomerRequestDto(String message) {
        this.message = message;
    }
}
