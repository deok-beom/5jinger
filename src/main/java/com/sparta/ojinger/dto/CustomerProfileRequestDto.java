package com.sparta.ojinger.dto;

import lombok.Getter;

import java.util.Objects;

@Getter
public class CustomerProfileRequestDto {

    private String nickname;
    private String image;

    public CustomerProfileRequestDto(String nickname, String image) {
        this.nickname = Objects.requireNonNullElse(nickname, "");
        this.image = Objects.requireNonNullElse(image, "");
    }

}
