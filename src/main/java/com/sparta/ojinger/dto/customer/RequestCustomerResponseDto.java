package com.sparta.ojinger.dto.customer;

import com.sparta.ojinger.entity.CustomerRequest;
import lombok.Getter;

@Getter
public class RequestCustomerResponseDto {

    private Long requestId;

    private String sellerUsername;

    private Long userId;

    private String message;

    private boolean status;


    public RequestCustomerResponseDto(CustomerRequest customerRequest) {
        this.requestId = customerRequest.getId();
        this.sellerUsername = customerRequest.getSellerUsername();
        this.userId = customerRequest.getUserId();
        this.message = customerRequest.getMessage();
        this.status = customerRequest.isStatus();
    }
}
