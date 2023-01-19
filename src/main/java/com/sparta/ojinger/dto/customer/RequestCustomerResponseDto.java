package com.sparta.ojinger.dto.customer;

import com.sparta.ojinger.entity.CustomerRequest;
import com.sparta.ojinger.entity.ProcessStatus;
import lombok.Getter;

@Getter
public class RequestCustomerResponseDto {

    private Long requestId;

    private String sellerUsername;

    private Long customerId;

    private String message;

    private ProcessStatus status;


    public RequestCustomerResponseDto(CustomerRequest customerRequest) {
        this.requestId = customerRequest.getId();
        //this.sellerUsername = customerRequest.getSellerUsername();
        this.customerId = customerRequest.getUserId();
        this.message = customerRequest.getMessage();
        this.status = customerRequest.getStatus();
    }
}
