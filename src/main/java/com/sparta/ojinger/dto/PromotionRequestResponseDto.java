package com.sparta.ojinger.dto;

import com.sparta.ojinger.entity.ProcessStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PromotionRequestResponseDto {
    private final Long requestId;
    private final Long userId;
    private final LocalDateTime requestDate;
    private final ProcessStatus status;

    public PromotionRequestResponseDto(Long requestId, Long userId, LocalDateTime requestDate, ProcessStatus status) {
        this.requestId = requestId;
        this.userId = userId;
        this.requestDate = requestDate;
        this.status = status;
    }
}
