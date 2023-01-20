package com.sparta.ojinger.controller;

import com.sparta.ojinger.dto.operator.CustomerResponseDto;
import com.sparta.ojinger.dto.PromotionRequestResponseDto;
import com.sparta.ojinger.dto.operator.SellerResponseDto;
import com.sparta.ojinger.entity.ProcessStatus;
import com.sparta.ojinger.service.PromotionRequestService;
import com.sparta.ojinger.service.ConvenienceService;
import com.sparta.ojinger.service.SellerService;
import com.sparta.ojinger.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/operators")
public class OperatorController {
    private final UserService userService;
    private final SellerService sellerService;
    private final PromotionRequestService promotionRequestService;
    private final ConvenienceService convenienceService;

    @GetMapping("/customers")
    public List<CustomerResponseDto> getAllCustomers(@RequestParam int page) {
        return userService.getAllCustomers(PageConfig.pageableSetting(page));
    }

    @GetMapping("/sellers")
    public List<SellerResponseDto> getAllSellers(@RequestParam int page) {
        return sellerService.getSellersForOperator(PageConfig.pageableSetting(page));
    }

    @GetMapping("/promotions")
    public List<PromotionRequestResponseDto> getAllPromotionRequests(@RequestParam int page) {
        return promotionRequestService.getAllPromotionRequests(PageConfig.pageableSetting(page));
    }

    @PostMapping("/promotions/{id}")
    public ResponseEntity<String> approvePromotionRequest(@PathVariable Long id) {
        convenienceService.promoteCustomerToSeller(id);
        return new ResponseEntity<>("등업을 승인하고 등업 처리하였습니다.", HttpStatus.OK);
    }

    @PatchMapping("/promotions/{id}")
    public ResponseEntity<String> rejectPromotionRequest(@PathVariable Long id) {
        promotionRequestService.updatePromotionRequestStatus(id, ProcessStatus.REJECTED);
        return new ResponseEntity<>("등업 반려 하였습니다.", HttpStatus.OK);
    }

    @PatchMapping("/seller/{id}")
    public ResponseEntity<String> demoteSellerToCustomer(@PathVariable Long id) {
        convenienceService.demoteSellerToCustomer(id);
        return new ResponseEntity<>("회원 등급을 강등 하였습니다.", HttpStatus.OK);
    }
}
