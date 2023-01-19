package com.sparta.ojinger.controller;



import com.sparta.ojinger.dto.customer.*;
import com.sparta.ojinger.security.UserDetailsImpl;
import com.sparta.ojinger.service.CustomerService;
import com.sparta.ojinger.service.CustomerServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerServiceImpl customerServiceImpl;

    //프로필 생성
    @PutMapping("/profile")
    public ResponseEntity<String> createProfile(@RequestBody CustomerProfileRequestDto customerProfileRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info(userDetails.getUser().toString());
        customerServiceImpl.createProfile(customerProfileRequestDto,userDetails.getUser());
        return new ResponseEntity<>("프로필 생성완료", HttpStatus.CREATED);
    }

    //프로필 조회
    @GetMapping("/profile")
    public ResponseEntity<CustomerProfileResponseDto> lookUpProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(customerServiceImpl.lookUpProfile(userDetails.getUser()),HttpStatus.OK);
    }

    //판매자 전체 조회
    @GetMapping("/sellers/{pageChoice}")
    public List<LookUpSellersResponseDto> lookUpSellersList(@PathVariable int pageChoice) {
        return customerServiceImpl.lookUpSellersList(pageChoice);
    }

    //판매자 조회
    @GetMapping("/seller/{id}")
    public LookUpSellerResponseDto lookUpSeller(@PathVariable Long id) {
        return customerServiceImpl.lookUpSeller(id);
    }

    //판매자에게 요청
    @PostMapping("/sellers/{id}/request")
    public ResponseEntity<String> sellerRequest(@PathVariable Long id, @RequestBody SellerElevationsRequestDto sellerElevationsRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        customerServiceImpl.sellerRequest(id,sellerElevationsRequestDto,userDetails.getUser());
        return new ResponseEntity<>("요청 완료",HttpStatus.OK);
    }

//    //판매자에게 요청 취소
    @DeleteMapping("/sellers/{id}/request")
    public ResponseEntity<String> sellerCancelRequset(@PathVariable Long id) {
        customerServiceImpl.sellerCancelRequest(id);
        return new ResponseEntity<>("요청 취소",HttpStatus.OK);
    }

    //판매자 권한 요청
    @PostMapping("/elevations/{id}")
    public ResponseEntity<String> elevationsRequest(@PathVariable Long id) {
        customerServiceImpl.elevationsRequest(id);
        return new ResponseEntity<>("등록 요청 완료",HttpStatus.OK);
    }
}
