package com.sparta.ojinger.dto;

import com.sparta.ojinger.entity.UserRoleEnum;

import java.time.LocalDateTime;

public class SellerResponseDto extends CustomerResponseDto {
    private final String intro;
    private final String category;

    public SellerResponseDto(Long id, String username, String nickname, String image, LocalDateTime signUpDate, String intro, String category) {
        super(id, username, nickname, image, signUpDate);
        super.role = UserRoleEnum.SELLER;
        this.intro = intro;
        this.category = category;
    }
}
