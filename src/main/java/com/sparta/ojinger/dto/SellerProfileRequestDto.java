package com.sparta.ojinger.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SellerProfileRequestDto {
    private final String nickname;
    private final String image;
    private final String intro;
    private final String category;
}
