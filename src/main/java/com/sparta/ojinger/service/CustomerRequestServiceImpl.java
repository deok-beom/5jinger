package com.sparta.ojinger.service;


import com.sparta.ojinger.dto.RequestCustomerRequestDto;
import com.sparta.ojinger.dto.seller.RequestCustomerResponseDto;
import com.sparta.ojinger.entity.*;
import com.sparta.ojinger.exception.CustomException;
import com.sparta.ojinger.exception.ErrorCode;
import com.sparta.ojinger.repository.CustomerRequestRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Proc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomerRequestServiceImpl implements CustomerRequestService {
    private final CustomerRequestRepository customerRequestRepository;

    @Transactional(readOnly = true)
    public List<RequestCustomerResponseDto> getMyRequestList(Long userId, Pageable pageable) {
        Page<CustomerRequest> requests = customerRequestRepository.findAllByUserId(userId, pageable);
        return validPageAndCreateResponseDtoList(requests);
    }

    public List<RequestCustomerResponseDto> getMyRequestListByItem(Long itemId, Long userId, Pageable pageable) {
        Page<CustomerRequest> requests = customerRequestRepository.findAllByUserIdAndItemId(userId, itemId, pageable);
        return validPageAndCreateResponseDtoList(requests);
    }
    
    @Transactional
    public void createCustomerRequest(Long itemId, RequestCustomerRequestDto requestCustomerRequestDto, User user, Seller seller) {
        // 해당 아이템에 현재 사용자가 이미 요청한 내역이 있는지 확인한다.
        Optional<CustomerRequest> optionalCustomerRequest = customerRequestRepository.findByUserIdAndItemIdAndStatus(user.getId(), itemId, ProcessStatus.PENDING);
        if (optionalCustomerRequest.isPresent()) {
            throw new CustomException(ErrorCode.REQUEST_IS_EXIST);
        }

        // 새로운 요청을 생성한다.
        CustomerRequest customerRequest = new CustomerRequest(itemId, requestCustomerRequestDto.getMessage(), ProcessStatus.PENDING, user.getId(), seller.getId(), seller.getUser().getNickname());
        customerRequestRepository.save(customerRequest);
    }

    @Transactional
    public void cancelCustomerRequest(Long requestId, User user) {
        // 해당 아이템에 현재 사용자가 요청한 내역이 있는지 확인한다.
        Optional<CustomerRequest> optionalCustomerRequest = customerRequestRepository.findById(requestId);
        if (optionalCustomerRequest.isEmpty()) {
            throw new CustomException(ErrorCode.REQUEST_IS_NOT_EXIST);
        }

        CustomerRequest request = optionalCustomerRequest.get();
        // 요청을 한 사람이 현재 사용자가 맞는지 검증한다.
        if (request.getUserId() != user.getId()) {
            throw new CustomException(ErrorCode.INVALID_AUTH_TOKEN);
        }

        // 요청이 현재 대기중인 상태인지 검증한다.
        if (!request.getStatus().equals(ProcessStatus.PENDING)) {
            throw new CustomException(ErrorCode.REQUEST_IS_EXPIRED);
        }

        // 요청의 상태를 취소(CANCELED)로 바꾼다.
        request.setStatus(ProcessStatus.CANCELED);
    }

    // 구매자 요청 리스트 조회
    @Transactional
    public List<RequestCustomerResponseDto> getReceivedRequestList(Long sellerId, Pageable pageable) {
        // 현재 사용자에게 온 요청을 검색한다.
        Page<CustomerRequest> requests = customerRequestRepository.findAllBySellerId(sellerId, pageable);
        return validPageAndCreateResponseDtoList(requests);
    }


    //구매자 요청 수락
    @Transactional
    public void approveCustomerRequest(Long requestId, Long userId) {
        CustomerRequest request = validRequests(requestId, userId);
        request.setStatus(ProcessStatus.APPROVED);
        customerRequestRepository.save(request);
    }

    //구매자 요청 거절
    @Transactional
    public void rejectCustomerRequest(Long requestId, Long userId) {
        CustomerRequest customerRequest = validRequests(requestId, userId);

        if (customerRequest.getStatus().equals(ProcessStatus.CANCELED)) {
            throw new CustomException(ErrorCode.REQUEST_IS_REJECTED);
        }

        customerRequest.setStatus(ProcessStatus.REJECTED);
        customerRequestRepository.save(customerRequest);
    }

    private CustomerRequest validRequests(Long requestId, Long userId) {
        CustomerRequest customerRequest = customerRequestRepository.findById(requestId).orElseThrow(() -> new CustomException(ErrorCode.REQUEST_IS_NOT_EXIST));
        // 요청 대상 판매자가 현재 로그인한 사용자가 맞는지 확인한다.
        if (customerRequest.getSellerId() != userId) {
            throw new CustomException(ErrorCode.INVALID_AUTH_TOKEN);
        }

        // 요청이 이미 처리된 것은 아닌(PENDING 상태)지 검증한다.
        if (!customerRequest.getStatus().equals(ProcessStatus.PENDING)) {
            throw new CustomException(ErrorCode.REQUEST_IS_EXPIRED);
        }

        return customerRequest;
    }

    private List<RequestCustomerResponseDto> validPageAndCreateResponseDtoList(Page<CustomerRequest> requests) {
        if (requests.isEmpty()) {
            throw new CustomException(ErrorCode.PAGINATION_IS_NOT_EXIST);
        }

        // DTO에 담아 반환한다.
        List<RequestCustomerResponseDto> requestCustomerResponseDtoList = new ArrayList<>();
        for (CustomerRequest request : requests) {
            requestCustomerResponseDtoList.add(new RequestCustomerResponseDto(request));
        }
        return requestCustomerResponseDtoList;
    }
}
