package com.sparta.ojinger.dto.seller;

import lombok.Getter;

import java.util.Objects;

@Getter
public class ItemRequestDto {
    private final String title;
    private final String content;
    private final String category;
    private final Long price;

    public ItemRequestDto(String title, String content, String category, Long price) {
        this.title = Objects.requireNonNullElse(title, "");
        this.content = Objects.requireNonNullElse(content, "");
        this.category = Objects.requireNonNullElse(category, "");
        this.price = (price == null ? 0 : price);
    }
}
