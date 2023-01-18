package com.sparta.ojinger.service;



import com.sparta.ojinger.dto.customer.*;
import com.sparta.ojinger.entity.User;

import java.util.List;

public interface CustomerService {

    void createProfile(CustomerProfileRequestDto customerProfileRequestDto, User user);

    CustomerProfileResponseDto lookUpProfile(User user);

    List<LookUpSellersResponseDto> lookUpSellersList(int pageChoice);

    LookUpSellerResponseDto lookUpSeller(Long id);

    void sellerRequest(Long id, SellerElevationsRequestDto sellerElevationsRequestDto,User user);

    void sellerCancelRequest(Long id);
//
     void elevationsRequest(Long id);
}
