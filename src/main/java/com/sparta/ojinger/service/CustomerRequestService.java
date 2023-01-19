package com.sparta.ojinger.service;


import com.sparta.ojinger.dto.customer.*;
import com.sparta.ojinger.entity.Seller;
import com.sparta.ojinger.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerRequestService {

    List<RequestCustomerResponseDto> customerRequestList(Long requestId, Pageable pageable);

    void createCustomerRequest(Long itemId, RequestCustomerRequestDto requestCustomerRequestDto, User user, Long sellerId);

    void cancelCustomerRequest(Long requestId, User user);

    void customerRequestAccept(Long requestId, Seller seller);

    void customerRequestReject(Long requestId, Seller seller);
}
