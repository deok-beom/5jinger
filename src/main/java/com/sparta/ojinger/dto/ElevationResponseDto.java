package com.sparta.ojinger.dto;

import com.sparta.ojinger.entity.ElevationStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ElevationResponseDto {
    private final Long requestId;
    private final Long userId;
    private final LocalDateTime requestDate;
    private final ElevationStatus status;

    public ElevationResponseDto(Long requestId, Long userId, LocalDateTime requestDate, ElevationStatus status) {
        this.requestId = requestId;
        this.userId = userId;
        this.requestDate = requestDate;
        this.status = status;
    }
}
