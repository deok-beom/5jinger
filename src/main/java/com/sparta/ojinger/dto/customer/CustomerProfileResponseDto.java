package com.sparta.ojinger.dto.customer;

import com.sparta.ojinger.entity.User;
import lombok.Getter;

@Getter
public class CustomerProfileResponseDto {

    private final Long userId;

    private final String nickName;

    private final String image;

    public CustomerProfileResponseDto(User user) {
        this.userId = user.getId();
        this.nickName = user.getNickname();
        this.image = user.getImage();
    }
}
