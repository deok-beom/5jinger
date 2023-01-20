package com.sparta.ojinger.dto.seller;

import com.sparta.ojinger.entity.Seller;
import com.sparta.ojinger.entity.UserRoleEnum;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SellerProfileResponseDto {

    private final Long sellerId;
    private final LocalDateTime promotedAt;
    private final LocalDateTime lastModifiedDate;

    private final String nickName;

    private final String image;

    private String category;
    private final String intro;

    private UserRoleEnum role;

    public SellerProfileResponseDto(Seller seller) {
        this.sellerId = seller.getId();
        this.promotedAt = seller.getCreateAt();
        this.lastModifiedDate = seller.getModifiedAt();
        this.nickName = seller.getUser().getNickname();
        this.image = seller.getUser().getImage();
        this.category = seller.getCategoriesToString();
        this.intro = seller.getIntro();
        this.role = seller.getUser().getRole();
    }
}
