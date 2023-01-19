package com.sparta.ojinger.dto.operator;

import com.sparta.ojinger.entity.User;
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

    public CustomerResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
        this.nickname = user.getNickname();
        this.image = user.getImage();
        this.signUpDate = user.getCreateAt();
    }
}
