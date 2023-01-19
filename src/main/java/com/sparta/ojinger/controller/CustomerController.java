package com.sparta.ojinger.controller;


import com.sparta.ojinger.dto.ItemResponseDto;
import com.sparta.ojinger.dto.customer.*;
import com.sparta.ojinger.entity.Seller;
import com.sparta.ojinger.entity.User;
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
public class CustomerController {

    private final CustomerRequestRequestServiceImpl customerService;
    private final ItemService itemService;
    private final SellerService sellerService;
    private final PromotionRequestService promotionRequestService;
    private final UserService userService;

    // 판매자 전체 조회
    @GetMapping("/sellers")
    public List<LookUpSellersResponseDto> lookUpSellersList(@RequestParam int page) {
        return sellerService.lookUpSellersList(pageableSetting(page));
    }

    // 판매자 조회
    @GetMapping("/sellers/{id}")
    public LookUpSellerResponseDto lookUpSeller(@PathVariable Long id) {
        return sellerService.lookUpSeller(id);
    }

    // 판매자 권한 등업 요청
    @PostMapping("/requests/promotion")
    public ResponseEntity<String> requestPromotion(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User customer = userService.getUserById(userDetails.getUser().getId());
        promotionRequestService.requestPromotion(customer);
        return new ResponseEntity<>("등록 요청 완료", HttpStatus.OK);
    }

    // 판매자에게 요청
    @PostMapping("/requests/items/{id}")
    public ResponseEntity<String> customerRequest(@PathVariable Long id, @RequestBody RequestCustomerRequestDto requestCustomerRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ItemResponseDto item = itemService.getItemById(id);
        Seller seller = sellerService.getSellerByUserId(item.getSellerId());
        if (seller.getUser().getId() == userDetails.getUser().getId()) {
            // 판매자 자신이 자신의 물건에 요청할 수 없다
            throw new IllegalArgumentException();
        }
        customerService.createCustomerRequest(id, requestCustomerRequestDto, userDetails.getUser(), item.getSellerId());
        return new ResponseEntity<>("요청 완료", HttpStatus.OK);
    }

    // 판매자에게 요청 취소
    @PatchMapping("/requests/{id}/cancel")
    public ResponseEntity<String> customerCancelRequest(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        customerService.cancelCustomerRequest(id, userDetails.getUser());
        return new ResponseEntity<>("요청 취소", HttpStatus.OK);
    }
}
