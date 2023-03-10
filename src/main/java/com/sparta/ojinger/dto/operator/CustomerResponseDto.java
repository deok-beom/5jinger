package com.sparta.ojinger.dto.operator;

import com.sparta.ojinger.entity.User;
import com.sparta.ojinger.entity.UserRoleEnum;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CustomerResponseDto {
    private final Long id;
    private final LocalDateTime signUpDate;
    private final LocalDateTime lastModifiedDate;
    private final String username;
    private final String nickname;
    private final String image;
    protected UserRoleEnum role;


    public CustomerResponseDto(User user) {
        this.id = user.getId();
        this.signUpDate = user.getCreateAt();
        this.lastModifiedDate = user.getModifiedAt();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.image = user.getImage();
        this.role = user.getRole();
    }
}
