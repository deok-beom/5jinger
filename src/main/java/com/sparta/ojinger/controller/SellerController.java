package com.sparta.ojinger.controller;

import com.sparta.ojinger.dto.ItemRequestDto;
import com.sparta.ojinger.dto.SellerProfileRequestDto;
import com.sparta.ojinger.dto.SellerProfileResponseDto;
import com.sparta.ojinger.dto.SellerResponseDto;
import com.sparta.ojinger.entity.Seller;
import com.sparta.ojinger.security.UserDetailsImpl;
import com.sparta.ojinger.service.ItemService;
import com.sparta.ojinger.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {

    private final SellerService sellerService;
    private final ItemService itemService;

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
        Seller seller = sellerService.getSellerById(userDetails.getUser().getId());
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
}
