package com.sparta.ojinger.dto.customer;

import com.sparta.ojinger.entity.Seller;
import com.sparta.ojinger.entity.User;
import com.sparta.ojinger.entity.UserRoleEnum;
import lombok.Getter;

@Getter
public class LookUpSellerResponseDto {

    private final Long sellerId;

    private final String usename;

    private final String nickName;

    private String category;

    private UserRoleEnum role;

    public LookUpSellerResponseDto(Seller seller) {
        this.sellerId = seller.getId();
        this.usename = seller.getUser().getUsername();
        this.nickName = seller.getUser().getNickname();
        this.category = seller.getCategory();
        this.role = seller.getUser().getRole();
    }
}
