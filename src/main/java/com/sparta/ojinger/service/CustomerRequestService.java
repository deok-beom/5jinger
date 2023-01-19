package com.sparta.ojinger.service;


import com.sparta.ojinger.dto.seller.RequestCustomerResponseDto;
import com.sparta.ojinger.dto.user.*;
import com.sparta.ojinger.entity.Seller;
import com.sparta.ojinger.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerRequestService {

    List<RequestCustomerResponseDto> getMyCustomerRequestList(Long requestId, Pageable pageable);

    void createCustomerRequest(Long itemId, RequestCustomerRequestDto requestCustomerRequestDto, User user, Long sellerId);

    void cancelCustomerRequest(Long requestId, User user);

    void approveCustomerRequest(Long requestId, Seller seller);

    void customerRequestReject(Long requestId, Seller seller);
}
