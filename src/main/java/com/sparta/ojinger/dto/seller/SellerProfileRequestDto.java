package com.sparta.ojinger.dto.seller;

import com.sparta.ojinger.dto.CustomerProfileRequestDto;
import lombok.Getter;

import java.util.Objects;

@Getter
public class SellerProfileRequestDto extends CustomerProfileRequestDto {
    private final String intro;
    private final String category;

    public SellerProfileRequestDto(String nickname, String image, String intro, String category) {
        super(nickname, image);
        this.intro = Objects.requireNonNullElse(intro, "");
        this.category = Objects.requireNonNullElse(category, "");
    }
}
