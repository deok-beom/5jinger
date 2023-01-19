package com.sparta.ojinger.service;



import com.sparta.ojinger.dto.customer.*;
import com.sparta.ojinger.entity.User;

import java.util.List;

public interface CustomerService {

    void updateProfile(CustomerProfileRequestDto customerProfileRequestDto, User user);

    CustomerProfileResponseDto lookUpProfile(User user);

    List<LookUpSellersResponseDto> lookUpSellersList(int pageChoice);

    List<RequestCustomerResponseDto> customerRequestList(int pageChoice);

    LookUpSellerResponseDto lookUpSeller(Long id);

    void customerRequest(String username, RequestCustomerRequestDto requestCustomerRequestDto, User user);

    void customerCancelRequest(String username, User user);

    void customerRequestAccept(Long requestId, User user);

    void customerRequestReject(Long requestId, User user);

     void elevationsRequest(Long id);
}
