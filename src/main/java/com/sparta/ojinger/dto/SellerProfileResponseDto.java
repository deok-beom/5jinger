package com.sparta.ojinger.dto;

import com.sparta.ojinger.entity.Seller;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SellerProfileResponseDto {
    private String nickname;
    private String image;
    private String intro;
    private String category;

    public SellerProfileResponseDto(Seller seller){
        this.nickname = seller.getUser().getNickname();
        this.image = seller.getUser().getImage();
        this.intro = seller.getIntro();
        this.category = seller.getCategoriesToString();
    }
}
