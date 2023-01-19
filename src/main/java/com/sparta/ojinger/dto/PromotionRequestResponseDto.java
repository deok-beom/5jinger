package com.sparta.ojinger.dto;

import com.sparta.ojinger.entity.ProcessStatus;
import com.sparta.ojinger.entity.PromotionRequest;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PromotionRequestResponseDto {
    private final Long requestId;
    private final Long userId;
    private final LocalDateTime requestDate;
    private final ProcessStatus status;

    public PromotionRequestResponseDto(PromotionRequest request) {
        this.requestId = request.getId();
        this.userId = request.getUser().getId();
        this.requestDate = request.getRequestDate();
        this.status = request.getStatus();
    }
}
