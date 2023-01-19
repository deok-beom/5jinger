package com.sparta.ojinger.dto.seller;

import lombok.Getter;

import java.util.Objects;

@Getter
public class ItemRequestDto {
    private String title;
    private String content;
    private String category;
    private Long price;

    public ItemRequestDto(String title, String content, String category, Long price) {
        this.title = Objects.requireNonNullElse(title, "");
        this.content = Objects.requireNonNullElse(content, "");
        this.category = Objects.requireNonNullElse(category, "");
        this.price = (price == null ? 0 : price);
    }
}
