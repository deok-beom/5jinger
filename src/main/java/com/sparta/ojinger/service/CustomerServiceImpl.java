package com.sparta.ojinger.service;


import com.sparta.ojinger.dto.customer.*;
import com.sparta.ojinger.entity.*;
import com.sparta.ojinger.exception.CustomException;
import com.sparta.ojinger.exception.ErrorCode;
import com.sparta.ojinger.repository.CustomerRequestRepository;
import com.sparta.ojinger.repository.PromotionRequestRepository;
import com.sparta.ojinger.repository.SellerRepository;
import com.sparta.ojinger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final CustomerRequestRepository customerRequestRepository;
    private final PromotionRequestRepository promotionRequestRepository;

    //프로필 설정
    @Transactional
    public void updateProfile(CustomerProfileRequestDto customerProfileRequestDto, User user) {
        User customer = userRepository.findById(user.getId()).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
<<<<<<< HEAD
        customer.updateUserProfile(customerProfileRequestDto.getNickName(), customerProfileRequestDto.getImage());
=======
        customer.userChangeNicknameAndImage(customerProfileRequestDto.getNickName(),customerProfileRequestDto.getImage());
>>>>>>> 3034a0ebd0719d27199555929a4aedb469650625
        userRepository.save(customer);
    }

    //프로필 조회
    @Transactional
    public CustomerProfileResponseDto lookUpProfile(User user) {
        User customer = userRepository.findById(user.getId()).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        return new CustomerProfileResponseDto(customer);
    }

    //판매자 리스트 조회
    @Transactional
    public List<LookUpSellersResponseDto> lookUpSellersList(int pageChoice) {
        if (pageChoice < 1) {
            throw new CustomException(ErrorCode.PAGINATION_IS_NOT_EXIST);
        }
        Page<Seller> sellersPage = sellerRepository.findAll(pageableSetting(pageChoice));
        if (sellersPage.isEmpty()) {
            throw new CustomException(ErrorCode.PAGINATION_IS_NOT_EXIST);
        }
        List<LookUpSellersResponseDto> lookUpSellersResponseDtoList = new ArrayList<>();
        for (Seller seller : sellersPage) {
            lookUpSellersResponseDtoList.add(new LookUpSellersResponseDto(seller));
        }
        return lookUpSellersResponseDtoList;
    }


    //판매자 조회
    @Transactional
    public LookUpSellerResponseDto lookUpSeller(Long id) {
        Seller seller = sellerRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return new LookUpSellerResponseDto(seller);
    }

    //판매자에게 요청
    @Transactional
    public void customerRequest(String username, RequestCustomerRequestDto requestCustomerRequestDto, User user) {
        Optional<CustomerRequest> optionalCustomerRequest = customerRequestRepository.findByIdAndSellerUsername(user.getId(),username);
        if(optionalCustomerRequest.isPresent()) {
            throw new CustomException(ErrorCode.REQUEST_IS_EXIST);
        }
        CustomerRequest customerRequest = new CustomerRequest(username, requestCustomerRequestDto.getMessage(),false,user);
        customerRequestRepository.save(customerRequest);
    }

    //판매자에게 요청 취소
    @Transactional
    public void customerCancelRequest(String username, User user) {
        Optional<CustomerRequest> optionalCustomerRequest = customerRequestRepository.findByIdAndSellerUsername(user.getId(),username);
        if(optionalCustomerRequest.isEmpty()) {
            throw new CustomException(ErrorCode.REQUEST_IS_NOT_EXIST);
        }
        customerRequestRepository.delete(optionalCustomerRequest.get());
    }



    //판매자 권한 요청
    @Transactional
    public void elevationsRequest(Long id) {
        User customer = userRepository.findById(id).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        Optional<PromotionRequest> optionalElevation = promotionRequestRepository.findByUserId(id);
        if(optionalElevation.isPresent()) {
            throw new CustomException(ErrorCode.REQUEST_IS_EXIST);
        }
        //Elevation elevation = new Elevation(customer,ElevationStatus.PENDING);
//        elevation.updateSatus(ElevationStatus.PENDING);
        //elevationRepository.save(elevation);
    }

    // 구매자 요청 리스트 조회
    @Transactional
    public List<RequestCustomerResponseDto> customerRequestList(int pageChoice) {
        if (pageChoice < 1) {
            throw new CustomException(ErrorCode.PAGINATION_IS_NOT_EXIST);
        }
        Page<CustomerRequest> customerRequestPage = customerRequestRepository.findAll(pageableSetting(pageChoice));
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
    public void customerRequestAccept(Long requestId, User user) {
        CustomerRequest customerRequest = customerRequestRepository.findById(requestId).orElseThrow(()-> new CustomException(ErrorCode.REQUEST_IS_NOT_EXIST));
        Seller seller = sellerRepository.findByUser(user).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        Optional<CustomerRequest> optionalCustomerRequest = customerRequestRepository.findByIdAndSellerUsername(requestId,seller.getUser().getUsername());        if(optionalCustomerRequest.get().isStatus() == true) {
            throw  new CustomException(ErrorCode.REQUEST_IS_ACCEPT);
        }
        customerRequest.updateCustomerRequestStatus(true);
        customerRequestRepository.save(customerRequest);
    }

    //구매자 요청 거절
    @Transactional
    public void customerRequestReject(Long requestId, User user) {
        CustomerRequest customerRequest = customerRequestRepository.findById(requestId).orElseThrow(()-> new CustomException(ErrorCode.REQUEST_IS_NOT_EXIST));
        Seller seller = sellerRepository.findByUser(user).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        Optional<CustomerRequest> optionalCustomerRequest = customerRequestRepository.findByIdAndSellerUsername(requestId,seller.getUser().getUsername());
        if(optionalCustomerRequest.get().isStatus() == true) {
            throw  new CustomException(ErrorCode.REQUEST_IS_ACCEPT);
        }
        customerRequestRepository.delete(customerRequest);
    }

    //페이징
    public Pageable pageableSetting(int pageChoice) {
        Sort.Direction direction = Sort.Direction.DESC;
<<<<<<< HEAD
        //Sort sort = Sort.by(direction, "signUpDate");
        Pageable pageable = PageRequest.of(pageChoice - 1, 10);
=======
        Sort sort = Sort.by(direction, "id");
        Pageable pageable = PageRequest.of(pageChoice - 1, 10, sort);
>>>>>>> 3034a0ebd0719d27199555929a4aedb469650625
        return pageable;
    }

}
