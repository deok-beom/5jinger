package com.sparta.ojinger.controller;

import com.sparta.ojinger.dto.seller.ItemRequestDto;
import com.sparta.ojinger.dto.seller.SellerProfileRequestDto;
import com.sparta.ojinger.dto.seller.SellerProfileResponseDto;
import com.sparta.ojinger.dto.seller.RequestCustomerResponseDto;
import com.sparta.ojinger.entity.Category;
import com.sparta.ojinger.entity.Seller;
import com.sparta.ojinger.exception.CustomException;
import com.sparta.ojinger.exception.ErrorCode;
import com.sparta.ojinger.security.UserDetailsImpl;
import com.sparta.ojinger.service.*;
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
    private final CategoryService categoryService;
    private final CustomerRequestService customerRequestService;
    private final ConvenienceService convenienceService;

    @PatchMapping("/profile")
    public ResponseEntity<String> updateMySellerProfile(@RequestBody SellerProfileRequestDto sellerProfileRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<Category> categories = categoryService.getCategoryFromString(sellerProfileRequestDto.getCategory());
        sellerService.updateMySellerProfile(sellerProfileRequestDto, categories, userDetails.getUser());
        return new ResponseEntity<>("프로필 설정이 완료 되었습니다. ", HttpStatus.OK);
    }

    @GetMapping("/profile")
    public SellerProfileResponseDto getMySellerProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return sellerService.getMySellerProfileResponseDto(userDetails.getUser().getId());
    }

    // 나의 상품 등록
    @PostMapping("/item")
    public ResponseEntity<String> addItem(@RequestBody ItemRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 전달 받은 데이터가 올바르지 않으면 예외를 발생한다.
        if (requestDto.getTitle().trim().equals("") || requestDto.getContent().trim().equals("") || requestDto.getPrice() == 0) {
            throw new CustomException(ErrorCode.INVALID_CREATE_ITEM);
        }

        // 현재 사용자의 판매자 정보를 불러온다.
        Seller seller = sellerService.getSellerByUserId(userDetails.getUser().getId());

        List<Category> categories = categoryService.getCategoryFromString(requestDto.getCategory());
        itemService.addItem(requestDto, categories, seller);
        return new ResponseEntity<>("상품 등록이 완료되었습니다.", HttpStatus.CREATED);
    }

    // 나의 상품 수정
    @PatchMapping("/items/{id}/update")
    public ResponseEntity<String> updateItem(@RequestBody ItemRequestDto requestDto, @PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<Category> categories = categoryService.getCategoryFromString(requestDto.getCategory());
        itemService.updateItem(requestDto, categories, id, userDetails.getUser().getId());
        return new ResponseEntity<>("상품 정보 수정이 완료되었습니다.", HttpStatus.OK);
    }

    // 나의 상품 판매 중지
    @PatchMapping("/items/{id}/suspend")
    public ResponseEntity<String> suspendItem(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        convenienceService.suspendItem(id, userDetails.getUser());
        return new ResponseEntity<>("상품 삭제가 완료되었습니다.", HttpStatus.OK);
    }

    // 구매자 요청 목록 조회
    @GetMapping("/requests")
    public List<RequestCustomerResponseDto> getCustomerRequestList(@RequestParam int page, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return customerRequestService.getReceivedRequestList(userDetails.getUser().getId(), pageableSetting(page));
    }

    // 구매자 요청 수락
    @PatchMapping("/requests/{id}/approve")
    public ResponseEntity<String> approveCustomerRequest(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        customerRequestService.approveCustomerRequest(id, userDetails.getUser().getId());
        return new ResponseEntity<>("요청 수락완료", HttpStatus.ACCEPTED);
    }

    // 구매자 요청 거절
    @PatchMapping("/requests/{id}/reject")
    public ResponseEntity<String> customerRequestReject(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        customerRequestService.rejectCustomerRequest(id, userDetails.getUser().getId());
        return new ResponseEntity<>("요청 거절완료", HttpStatus.OK);
    }
}
