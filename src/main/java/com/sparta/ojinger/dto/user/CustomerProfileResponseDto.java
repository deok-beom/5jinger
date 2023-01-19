package com.sparta.ojinger.dto.user;

import com.sparta.ojinger.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CustomerProfileResponseDto {
    private final Long userId;
    private String username;
    private final String nickname;
    private final String image;
    private final LocalDateTime signUpDate;
    private final LocalDateTime lastModifiedDate;

    public CustomerProfileResponseDto(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.image = user.getImage();
        this.signUpDate = user.getCreateAt();
        this.lastModifiedDate = user.getModifiedAt();
    }
}
