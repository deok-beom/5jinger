package com.sparta.ojinger.service;


import com.sparta.ojinger.dto.customer.*;
import com.sparta.ojinger.entity.*;
import com.sparta.ojinger.exception.CustomException;
import com.sparta.ojinger.exception.ErrorCode;
import com.sparta.ojinger.repository.CustomerRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomerRequestRequestServiceImpl implements CustomerRequestService {
    private final CustomerRequestRepository customerRequestRepository;

    //판매자에게 요청
    @Transactional
    public void createCustomerRequest(Long itemId, RequestCustomerRequestDto requestCustomerRequestDto, User user, Long sellerId) {
        Optional<CustomerRequest> optionalCustomerRequest = customerRequestRepository.findByIdAndItemId(user.getId(), itemId);
        if (optionalCustomerRequest.isPresent()) {
            throw new CustomException(ErrorCode.REQUEST_IS_EXIST);
        }

        CustomerRequest customerRequest = new CustomerRequest(itemId, requestCustomerRequestDto.getMessage(), ProcessStatus.PENDING, user.getId(), sellerId);
        customerRequestRepository.save(customerRequest);
    }

    //판매자에게 요청 취소
    @Transactional
    public void cancelCustomerRequest(Long requestId, User user) {
        Optional<CustomerRequest> optionalCustomerRequest = customerRequestRepository.findById(requestId);
        if (optionalCustomerRequest.isEmpty()) {
            throw new CustomException(ErrorCode.REQUEST_IS_NOT_EXIST);
        }

        CustomerRequest request = optionalCustomerRequest.get();
        if (!request.getStatus().equals(ProcessStatus.PENDING)) {
            throw new CustomException(ErrorCode.REQUEST_IS_EXPIRED);
        }

        request.updateCustomerRequestStatus(ProcessStatus.CANCELED);
        customerRequestRepository.save(optionalCustomerRequest.get());
    }

    // 구매자 요청 리스트 조회
    @Transactional
    public List<RequestCustomerResponseDto> customerRequestList(Long id, Pageable pageable) {
        Page<CustomerRequest> customerRequestPage = customerRequestRepository.findAllBySellerId(id, pageable);
        if (customerRequestPage.isEmpty()) {
            throw new CustomException(ErrorCode.PAGINATION_IS_NOT_EXIST);
        }
        List<RequestCustomerResponseDto> requestCustomerResponseDtoList = new ArrayList<>();
        for (CustomerRequest customerRequest : customerRequestPage) {
            requestCustomerResponseDtoList.add(new RequestCustomerResponseDto(customerRequest));
        }
        return requestCustomerResponseDtoList;
    }


    //구매자 요청 수락
    @Transactional
    public void customerRequestAccept(Long requestId, Seller seller) {
        CustomerRequest customerRequest = validRequests(requestId, seller);

        if (customerRequest.getStatus().equals(ProcessStatus.APPROVED)) {
            throw new CustomException(ErrorCode.REQUEST_IS_ACCEPT);
        }

        customerRequest.updateCustomerRequestStatus(ProcessStatus.APPROVED);
        customerRequestRepository.save(customerRequest);
    }

    //구매자 요청 거절
    @Transactional
    public void customerRequestReject(Long requestId, Seller seller) {
        CustomerRequest customerRequest = validRequests(requestId, seller);

        if (customerRequest.getStatus().equals(ProcessStatus.CANCELED)) {
            throw new CustomException(ErrorCode.REQUEST_IS_REJECTED);
        }

        customerRequest.updateCustomerRequestStatus(ProcessStatus.REJECTED);
        customerRequestRepository.save(customerRequest);
    }

    private CustomerRequest validRequests(Long requestId, Seller seller) {
        CustomerRequest customerRequest = customerRequestRepository.findById(requestId).orElseThrow(() -> new CustomException(ErrorCode.REQUEST_IS_NOT_EXIST));
        if (customerRequest.getSellerId() != seller.getId()) {
            throw new IllegalArgumentException();
        }

        if (!customerRequest.getStatus().equals(ProcessStatus.PENDING)) {
            throw new CustomException(ErrorCode.REQUEST_IS_EXPIRED);
        }

        return customerRequest;
    }
}
