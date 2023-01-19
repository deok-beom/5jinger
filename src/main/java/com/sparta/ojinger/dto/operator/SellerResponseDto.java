package com.sparta.ojinger.dto.operator;

import com.sparta.ojinger.entity.Seller;
import com.sparta.ojinger.entity.UserRoleEnum;
import lombok.Getter;

@Getter
public class SellerResponseDto extends CustomerResponseDto {
    private final String intro;
    //private final String category;

    public SellerResponseDto(Seller seller) {
        super(seller.getUser());
        super.role = UserRoleEnum.SELLER;
        this.intro = seller.getIntro();
        //this.category = seller.getCategories();
    }
}
