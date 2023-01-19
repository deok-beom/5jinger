package com.sparta.ojinger.controller;

import com.sparta.ojinger.dto.ItemRequestDto;
import com.sparta.ojinger.dto.SellerProfileRequestDto;
import com.sparta.ojinger.dto.SellerProfileResponseDto;
import com.sparta.ojinger.dto.customer.RequestCustomerResponseDto;
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
    public ResponseEntity updateSellerProfile(@RequestBody SellerProfileRequestDto sellerProfileRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        sellerService.updateSellerProfile(sellerProfileRequestDto, userDetails);
        return new ResponseEntity<>("프로필 설정이 완료 되었습니다. ", HttpStatus.OK);
    }

    @GetMapping("/profile")
    public SellerProfileResponseDto getSellerProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return sellerService.getSellerProfile(userDetails);
    }

    // 아이템 등록
    @PostMapping("/item")
    public void addItem(@RequestBody ItemRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Seller seller = sellerService.getSellerByUserId(userDetails.getUser().getId());
        itemService.addItem(requestDto, seller);
    }

    @PatchMapping("/items/{id}")
    public void updateItem(@RequestBody ItemRequestDto requestDto, @PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        itemService.updateItem(requestDto, id, userDetails.getUser().getId());
    }

    @DeleteMapping("/items/{id}")
    public void deleteItem(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        itemService.deleteItem(id, userDetails.getUser());
    }

    // 구매자 요청 리스트 조회
    @GetMapping("/requests")
    public List<RequestCustomerResponseDto> lookUpCustomerRequestList(@RequestParam int page, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Seller seller = sellerService.getSellerByUserId(userDetails.getUser().getId());
        return customerRequestService.customerRequestList(seller.getId(), pageableSetting(page));
    }

    // 구매자 요청 수락
    @PatchMapping("/requests/{id}/accept")
    public ResponseEntity<String> customerRequestAccept(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Seller seller = sellerService.getSellerByUserId(userDetails.getUser().getId());
        customerRequestService.customerRequestAccept(id, seller);
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
