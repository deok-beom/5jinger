package com.sparta.ojinger.controller;

import com.sparta.ojinger.dto.ItemRequestDto;
import com.sparta.ojinger.dto.seller.SellerProfileRequestDto;
import com.sparta.ojinger.dto.SellerProfileResponseDto;
import com.sparta.ojinger.dto.seller.RequestCustomerResponseDto;
import com.sparta.ojinger.entity.Seller;
import com.sparta.ojinger.security.UserDetailsImpl;
import com.sparta.ojinger.service.CustomerRequestService;
import com.sparta.ojinger.service.ItemService;
import com.sparta.ojinger.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.ojinger.controller.PageConfig.pageableSetting;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {

    private final SellerService sellerService;
    private final ItemService itemService;
    private final CustomerRequestService customerRequestService;

    @PatchMapping("/profile")
    public ResponseEntity updateMySellerProfile(@RequestBody SellerProfileRequestDto sellerProfileRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        sellerService.updateMySellerProfile(sellerProfileRequestDto, userDetails);
        return new ResponseEntity<>("프로필 설정이 완료 되었습니다. ", HttpStatus.OK);
    }

    @GetMapping("/profile")
    public SellerProfileResponseDto getMySellerProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return sellerService.getMySellerProfile(userDetails);
    }

    // 나의 상품 등록
    @PostMapping("/item")
    public ResponseEntity<String> addItem(@RequestBody ItemRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Seller seller = sellerService.getSellerByUserId(userDetails.getUser().getId());
        itemService.addItem(requestDto, seller);
        return new ResponseEntity<>("상품 등록이 완료되었습니다.", HttpStatus.OK);
    }

    // 나의 상품 수정
    @PatchMapping("/items/{id}/update")
    public ResponseEntity<String> updateItem(@RequestBody ItemRequestDto requestDto, @PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        itemService.updateItem(requestDto, id, userDetails.getUser().getId());
        return new ResponseEntity<>("상품 정보 수정이 완료되었습니다.", HttpStatus.OK);
    }

    // 나의 상품 판매 중지
    @PatchMapping("/items/{id}/suspend")
    public ResponseEntity<String> suspendItem(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        itemService.suspendItem(id, userDetails.getUser());
        return new ResponseEntity<>("상품 삭제가 완료되었습니다.", HttpStatus.OK);
    }

    // 구매자 요청 목록 조회
    @GetMapping("/requests")
    public List<RequestCustomerResponseDto> getCustomerRequestList(@RequestParam int page, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Seller seller = sellerService.getSellerByUserId(userDetails.getUser().getId());
        return customerRequestService.getMyCustomerRequestList(seller.getId(), pageableSetting(page));
    }

    // 구매자 요청 수락
    @PatchMapping("/requests/{id}/approve")
    public ResponseEntity<String> approveCustomerRequest(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Seller seller = sellerService.getSellerByUserId(userDetails.getUser().getId());
        customerRequestService.approveCustomerRequest(id, seller);
        return new ResponseEntity<>("요청 수락완료", HttpStatus.ACCEPTED);
    }

    // 구매자 요청 거절
    @DeleteMapping("/requests/{id}")
    public ResponseEntity<String> customerRequestReject(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Seller seller = sellerService.getSellerByUserId(userDetails.getUser().getId());
        customerRequestService.customerRequestReject(id, seller);
        return new ResponseEntity<>("요청 거절완료", HttpStatus.OK);
    }
}
