package com.sparta.ojinger.dto;

import com.sparta.ojinger.entity.Seller;
import com.sparta.ojinger.entity.UserRoleEnum;
import lombok.Getter;

@Getter
public class LookUpSellerResponseDto {

    private final Long sellerId;

    private final String username;

    private final String nickName;

    private final String category;

    private final UserRoleEnum role;

    public LookUpSellerResponseDto(Seller seller) {
        this.sellerId = seller.getId();
        this.username = seller.getUser().getUsername();
        this.nickName = seller.getUser().getNickname();
        this.category = seller.getCategoriesToString();
        this.role = seller.getUser().getRole();
    }
}
