package com.sparta.ojinger.service;


import com.sparta.ojinger.dto.RequestCustomerRequestDto;
import com.sparta.ojinger.dto.seller.RequestCustomerResponseDto;
import com.sparta.ojinger.entity.Seller;
import com.sparta.ojinger.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerRequestService {

    List<RequestCustomerResponseDto> getMyRequestList(Long userId, Pageable pageable);
    List<RequestCustomerResponseDto> getMyRequestListByItem(Long itemId, Long userId, Pageable pageable);
    List<RequestCustomerResponseDto> getReceivedRequestList(Long requestId, Pageable pageable);

    void createCustomerRequest(Long itemId, RequestCustomerRequestDto requestCustomerRequestDto, User user, Seller seller);

    void cancelCustomerRequest(Long requestId, User user);

    void approveCustomerRequest(Long requestId, Long userId);

    void rejectCustomerRequest(Long requestId, Long userId);

    void canceledAllRequestsToSeller(Long sellerId);
    void canceledAllRequestsAboutItem(Long itemId);
}
