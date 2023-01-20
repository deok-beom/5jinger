package com.sparta.ojinger.controller;


import com.sparta.ojinger.dto.ItemResponseDto;
import com.sparta.ojinger.dto.RequestCustomerRequestDto;
import com.sparta.ojinger.dto.seller.RequestCustomerResponseDto;
import com.sparta.ojinger.dto.seller.SellerProfileResponseDto;
import com.sparta.ojinger.entity.Seller;
import com.sparta.ojinger.entity.TradeStatus;
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

    private final CustomerRequestServiceImpl customerService;
    private final ItemService itemService;
    private final SellerService sellerService;
    private final PromotionRequestService promotionRequestService;
    private final CustomerRequestService customerRequestService;

    // 전체 판매자 목록 조회
    @GetMapping("/sellers")
    public List<SellerProfileResponseDto> getSellers(@RequestParam int page) {
        return sellerService.getSellerProfiles(pageableSetting(page));
    }

    // 판매자 조회
    @GetMapping("/sellers/{id}")
    public SellerProfileResponseDto getSeller(@PathVariable Long id) {
        return sellerService.getSellerProfileById(id);
    }

    // 나의 요청 모두 조회
    @GetMapping("/requests")
    public List<RequestCustomerResponseDto> getAllMyRequests(@RequestParam int page, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return customerRequestService.getMyRequestList(userDetails.getUser().getId(), PageConfig.pageableSetting(page));
    }

    @GetMapping("/items/{id}/requests")
    public List<RequestCustomerResponseDto> getAllMyRequestsAboutItem(@PathVariable Long id, @RequestParam int page, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return customerRequestService.getMyRequestListByItem(id, userDetails.getUser().getId(), PageConfig.pageableSetting(page));
    }

    // 아이템에 대한 요청 작성
    @PostMapping("/items/{id}/request")
    public ResponseEntity<String> requestAboutItem(@PathVariable Long id, @RequestBody RequestCustomerRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 전달 받은 아이템 ID를 이용해 Item을 찾는다.
        ItemResponseDto item = itemService.getItemById(id);

        // 찾은 Item에서 판매자 정보를 찾는다.
        Seller seller = sellerService.getSellerByUserId(item.getSellerId());

        // 찾은 판매자 정보와 현재 요청을 보낸 사용자가 동일하면 예외 발생 (내가 등록한 상품에 내가 요청할 수 없음)
        if (seller.getUser().getId() == userDetails.getUser().getId()) {
            throw new CustomException(ErrorCode.INVALID_CUSTOMER_REQUEST);
        }

        // 품절됐거나 판매 중지된 상품에는 요청할 수 없다.
        if (item.getStatus().equals(TradeStatus.SOLD_OUT)) {
            throw new CustomException(ErrorCode.ITEM_IS_SOLD_OUT);
        }

        if (item.getStatus().equals(TradeStatus.SUSPENSION)) {
            throw new CustomException(ErrorCode.ITEM_IS_SUSPENDED);
        }

        customerService.createCustomerRequest(item.getId(), requestDto, userDetails.getUser(), seller);
        return new ResponseEntity<>("요청 완료", HttpStatus.OK);
    }

    // 아이템에 대한 요청 취소
    @PatchMapping("/items/{id}/request")
    public ResponseEntity<String> requestCancelAboutItem(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        customerService.cancelCustomerRequest(id, userDetails.getUser());
        return new ResponseEntity<>("요청 취소", HttpStatus.OK);
    }


    // 판매자 권한으로 등업 요청
    @PostMapping("/promotions/request")
    public ResponseEntity<String> requestPromotion(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User customer = userDetails.getUser();
        // 현재 로그인한 유저의 권한이 일반 고객(CUSTOMER)인지 검증한다.
        if (!customer.getRole().equals(UserRoleEnum.CUSTOMER)) {
            throw new CustomException(ErrorCode.NOT_CUSTOMERS);
        }

        promotionRequestService.requestPromotion(customer);
        return new ResponseEntity<>("등록 요청 완료", HttpStatus.OK);
    }
}
