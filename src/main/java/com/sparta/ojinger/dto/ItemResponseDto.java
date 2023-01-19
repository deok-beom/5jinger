package com.sparta.ojinger.dto;

import com.sparta.ojinger.entity.Item;
import com.sparta.ojinger.entity.TradeStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ItemResponseDto {
    private final Long id;
    private final Long sellerId;
    private final String sellerNickName;
    private final String title;
    private final String content;
    private final String category;
    private final Long price;
    private final TradeStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public ItemResponseDto(Item item) {
        this.id = item.getId();
        this.sellerId = item.getSeller().getId();
        this.sellerNickName = item.getSeller().getUser().getNickname();
        this.title = item.getTitle();
        this.content = item.getContent();
        this.category = item.getCategory();
        this.price = item.getPrice();
        this.status = item.getStatus();
        this.createdAt = item.getCreateAt();
        this.modifiedAt = item.getModifiedAt();
    }
}
