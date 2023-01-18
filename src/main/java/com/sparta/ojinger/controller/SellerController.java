package com.sparta.ojinger.controller;

import com.sparta.ojinger.dto.SellerProfileResponseDto;
import com.sparta.ojinger.dto.SellerResponseDto;
import com.sparta.ojinger.security.UserDetailsImpl;
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

    @PatchMapping("/profile")
    public ResponseEntity updateSellerProfile(@RequestBody SellerProfileResponseDto sellerProfileResponseDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        sellerService.updateSellerProfile(sellerProfileResponseDto,userDetails);
        return new ResponseEntity<>("프로필 설정이 완료 되었습니다. ", HttpStatus.OK);
    }

    @GetMapping("/profile")
    public SellerProfileResponseDto getSellerProfile(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return sellerService.getSellerProfile(userDetails);
    }
}
