package com.sparta.ojinger.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ItemResponseDto {
    private final Long id;
    private final String title;
    private final String content;
    private final String category;
    private final Long price;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public ItemResponseDto(Long id, String title, String content, String category, Long price, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.price = price;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
