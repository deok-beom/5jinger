package com.sparta.ojinger.dto.seller;

import com.sparta.ojinger.entity.CustomerRequest;
import com.sparta.ojinger.entity.ProcessStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
public class RequestCustomerResponseDto {

    private final Long requestId;

    private final String sellerUsername;

    private final Long customerId;

    private final String message;

    private final ProcessStatus status;


    public RequestCustomerResponseDto(CustomerRequest customerRequest) {
        this.requestId = customerRequest.getId();
        this.customerId = customerRequest.getUserId();
        this.sellerUsername = customerRequest.getSellerNickname();
        this.message = customerRequest.getMessage();
        this.status = customerRequest.getStatus();
    }
}
