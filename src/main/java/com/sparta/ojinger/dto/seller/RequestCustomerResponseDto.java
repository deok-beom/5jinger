package com.sparta.ojinger.dto.seller;

import com.sparta.ojinger.entity.CustomerRequest;
import com.sparta.ojinger.entity.ProcessStatus;
import lombok.Getter;

@Getter
public class RequestCustomerResponseDto {

    private final Long requestId;
    private final Long itemId;
    private final Long sellerId;
    private final Long requesterId;
    private final String sellerUsername;

    private final String message;

    private final ProcessStatus status;


    public RequestCustomerResponseDto(CustomerRequest customerRequest) {
        this.requestId = customerRequest.getId();
        this.itemId = customerRequest.getItemId();
        this.sellerId = customerRequest.getSellerId();
        this.requesterId = customerRequest.getUserId();
        this.sellerUsername = customerRequest.getSellerNickname();
        this.message = customerRequest.getMessage();
        this.status = customerRequest.getStatus();
    }
}
