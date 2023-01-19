package com.sparta.ojinger.dto.customer;


import com.sparta.ojinger.entity.Seller;
import com.sparta.ojinger.entity.User;
import com.sparta.ojinger.entity.UserRoleEnum;
import lombok.Getter;

@Getter
public class LookUpSellersResponseDto {

    private final Long sellerId;

    private final String nickName;

    private final String image;

    private  String category;

    private UserRoleEnum role;

    public LookUpSellersResponseDto(Seller seller) {
        this.sellerId = seller.getId();
        this.nickName = seller.getUser().getNickname();
        this.image = seller.getUser().getImage();
        //this.category = seller.getCategory();
        this.role = seller.getUser().getRole();
    }
}
