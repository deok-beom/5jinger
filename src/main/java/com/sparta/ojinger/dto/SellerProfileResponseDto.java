package com.sparta.ojinger.dto;

import com.sparta.ojinger.entity.Seller;
import com.sparta.ojinger.entity.UserRoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class SellerProfileResponseDto {

    private final Long sellerId;

    private final String nickName;

    private final String image;

    private  String category;

    private UserRoleEnum role;

    public SellerProfileResponseDto(Seller seller) {
        this.sellerId = seller.getId();
        this.nickName = seller.getUser().getNickname();
        this.image = seller.getUser().getImage();
        //this.category = seller.getCategory();
        this.role = seller.getUser().getRole();
    }
}
