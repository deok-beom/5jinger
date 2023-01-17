package com.sparta.ojinger.dto;

public class PageRequestDto {
    private int page;
    private int recordSize;
    private int pageSize;

    public PageRequestDto(int page, int recordSize, int pageSize) {
        this.page = page;
        this.recordSize = recordSize;
        this.pageSize = pageSize;
    }
}
