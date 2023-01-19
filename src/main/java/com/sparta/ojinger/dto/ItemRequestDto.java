package com.sparta.ojinger.dto;

import lombok.Getter;

@Getter
public class ItemRequestDto {
    private String title;
    private String content;
    private String category;
    private Long price;

    public ItemRequestDto(String title, String content, String category, Long price) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.price = price;
    }
}
