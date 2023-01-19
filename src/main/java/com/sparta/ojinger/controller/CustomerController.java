package com.sparta.ojinger.controller;



import com.sparta.ojinger.dto.customer.*;
import com.sparta.ojinger.security.UserDetailsImpl;
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
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerServiceImpl customerService;

    //프로필 생성
    @PatchMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestBody CustomerProfileRequestDto customerProfileRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info(userDetails.getUser().toString());
        customerService.updateProfile(customerProfileRequestDto,userDetails.getUser());
        return new ResponseEntity<>("프로필 생성완료", HttpStatus.CREATED);
    }

    //프로필 조회
    @GetMapping("/profile")
    public ResponseEntity<CustomerProfileResponseDto> lookUpProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(customerService.lookUpProfile(userDetails.getUser()),HttpStatus.OK);
    }

    //판매자 전체 조회
    @GetMapping("/sellers/{pageChoice}/page")
    public List<LookUpSellersResponseDto> lookUpSellersList(@PathVariable int pageChoice) {
        return customerService.lookUpSellersList(pageChoice);
    }

    //판매자 조회
    @GetMapping("/sellers/{id}")
    public LookUpSellerResponseDto lookUpSeller(@PathVariable Long id) {
        return customerService.lookUpSeller(id);
    }

    //판매자에게 요청
    @PostMapping("/sellers/request")
    public ResponseEntity<String> customerRequest(@RequestParam String username, @RequestBody RequestCustomerRequestDto requestCustomerRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        customerService.customerRequest(username, requestCustomerRequestDto,userDetails.getUser());
        return new ResponseEntity<>("요청 완료",HttpStatus.OK);
    }

//    //판매자에게 요청 취소
    @DeleteMapping("/sellers/request")
    public ResponseEntity<String> customerCancelRequest(@RequestParam String username, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        customerService.customerCancelRequest(username, userDetails.getUser());
        return new ResponseEntity<>("요청 취소",HttpStatus.OK);
    }

    //판매자 권한 요청
    @PostMapping("/elevations/{id}")
    public ResponseEntity<String> elevationsRequest(@PathVariable Long id) {
        customerService.elevationsRequest(id);
        return new ResponseEntity<>("등록 요청 완료",HttpStatus.OK);
    }

    //구매자 요청 리스트 조회
    @GetMapping("/sellers/request/{pageChoice}/page")
    public List<RequestCustomerResponseDto> lookUpCustomerRequestList(@PathVariable int pageChoice) {
        return customerService.customerRequestList(pageChoice);
    }

    //구매자 요청 수락
    @PatchMapping("/sellers/request/accept/{requestId}")
    public ResponseEntity<String> customerRequestAccept(@PathVariable Long requestId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        customerService.customerRequestAccept(requestId,userDetails.getUser());
        return new ResponseEntity<>("요청 수락완료", HttpStatus.ACCEPTED);
    }

    //구매자 요청 거절
    @DeleteMapping("sellers/request/reject/{requestId}")
    public ResponseEntity<String> customerRequestReject(@PathVariable Long requestId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        customerService.customerRequestReject(requestId,userDetails.getUser());
        return new ResponseEntity<>("요청 거절완료",HttpStatus.OK);
    }
}
