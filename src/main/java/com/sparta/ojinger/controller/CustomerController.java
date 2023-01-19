package com.sparta.ojinger.controller;


import com.sparta.ojinger.dto.ItemResponseDto;
import com.sparta.ojinger.dto.SellerProfileResponseDto;
import com.sparta.ojinger.dto.user.*;
import com.sparta.ojinger.entity.Seller;
import com.sparta.ojinger.entity.User;
import com.sparta.ojinger.entity.UserRoleEnum;
import com.sparta.ojinger.exception.CustomException;
import com.sparta.ojinger.exception.ErrorCode;
import com.sparta.ojinger.security.UserDetailsImpl;
import com.sparta.ojinger.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.ojinger.controller.PageConfig.pageableSetting;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerRequestRequestServiceImpl customerService;
    private final ItemService itemService;
    private final SellerService sellerService;
    private final PromotionRequestService promotionRequestService;
    private final UserService userService;

    // 전체 판매자 목록 조회
    @GetMapping("/sellers")
    public List<SellerProfileResponseDto> getSellers(@RequestParam int page) {
        return sellerService.getSellers(pageableSetting(page));
    }

    // 판매자 조회
    @GetMapping("/sellers/{id}")
    public SellerProfileResponseDto getSeller(@PathVariable Long id) {
        return sellerService.getSellerById(id);
    }

    // 판매자에게 요청
    @PostMapping("/requests/items/{id}")
    public ResponseEntity<String> requestToCustomerAboutItem(@PathVariable Long id, @RequestBody RequestCustomerRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ItemResponseDto item = itemService.getItemById(id);
        Seller seller = sellerService.getSellerByUserId(item.getSellerId());
        if (seller.getUser().getId() == userDetails.getUser().getId()) {
            throw new CustomException(ErrorCode.INVALID_CUSTOMER_REQUEST);
        }
        customerService.createCustomerRequest(id, requestDto, userDetails.getUser(), item.getSellerId());
        return new ResponseEntity<>("요청 완료", HttpStatus.OK);
    }

    // 판매자에게 요청 취소
    @PatchMapping("/requests/{id}/cancel")
    public ResponseEntity<String> customerCancelRequest(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        customerService.cancelCustomerRequest(id, userDetails.getUser());
        return new ResponseEntity<>("요청 취소", HttpStatus.OK);
    }


    // 판매자 권한 등업 요청
    @PostMapping("/requests/promotion")
    public ResponseEntity<String> requestPromotion(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User customer = userService.getUserByName(userDetails.getUsername());
        if (!customer.getRole().equals(UserRoleEnum.CUSTOMER)) {
            throw new CustomException(ErrorCode.NOT_CUSTOMERS);
        }
        promotionRequestService.requestPromotion(customer);
        return new ResponseEntity<>("등록 요청 완료", HttpStatus.OK);
    }
}
