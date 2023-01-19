package com.sparta.ojinger.dto.customer;

import com.sparta.ojinger.entity.User;
import lombok.Getter;

@Getter
public class SellerElevationsRequestDto {

    // Request id
    private Long id;

    private User user;

    private String message;
}
