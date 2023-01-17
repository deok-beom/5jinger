package com.sparta.ojinger.dto;

import com.sparta.ojinger.entity.UserRoleEnum;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CustomerResponseDto {
    private final Long id;
    private final String username;
    protected UserRoleEnum role;
    private final String nickname;
    private final String image;
    private final LocalDateTime signUpDate;

    public CustomerResponseDto(Long id, String username, String nickname, String image, LocalDateTime signUpDate) {
        this.id = id;
        this.username = username;
        this.role = UserRoleEnum.CUSTOMER;
        this.nickname = nickname;
        this.image = image;
        this.signUpDate = signUpDate;
    }
}
