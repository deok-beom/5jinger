package com.sparta.ojinger.controller;

import com.sparta.ojinger.dto.operator.CustomerResponseDto;
import com.sparta.ojinger.dto.PromotionRequestResponseDto;
import com.sparta.ojinger.dto.operator.SellerResponseDto;
import com.sparta.ojinger.entity.ProcessStatus;
import com.sparta.ojinger.entity.Seller;
import com.sparta.ojinger.entity.User;
import com.sparta.ojinger.entity.UserRoleEnum;
import com.sparta.ojinger.exception.CustomException;
import com.sparta.ojinger.service.PromotionRequestService;
import com.sparta.ojinger.service.OperatorService;
import com.sparta.ojinger.service.SellerService;
import com.sparta.ojinger.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.ojinger.exception.ErrorCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/operators")
public class OperatorController {
    private final UserService userService;
    private final SellerService sellerService;
    private final PromotionRequestService promotionRequestService;
    private final OperatorService operatorService;

    @GetMapping("/customers")
    public List<CustomerResponseDto> getAllCustomers(@PageableDefault(size = 5) Pageable pageable) {
        return userService.getAllCustomers(pageable);
    }

    @GetMapping("/sellers")
    public List<SellerResponseDto> getAllSellers(@PageableDefault(size = 5) Pageable pageable) {
        return sellerService.getAllSellers(pageable);
    }

    @GetMapping("/promotions")
    public List<PromotionRequestResponseDto> getAllPromotionRequests(@PageableDefault(size = 5) Pageable pageable) {
        return promotionRequestService.getAllPromotionRequests(pageable);
    }

    @PostMapping("/promotions/{id}")
    public ResponseEntity approvePromotionRequest(@PathVariable Long id) {
        operatorService.promoteCustomerToSeller(id);
        return new ResponseEntity<>("등업 성공", HttpStatus.OK);
    }

    @PatchMapping("/promotions/{id}")
    public void rejectPromotionRequest(@PathVariable Long id) {
        promotionRequestService.updatePromotionRequestStatus(id, ProcessStatus.REJECTED);
    }

    @PatchMapping("/seller/{id}/demotion")
    public void demoteSellerToCustomer(@PathVariable Long id) {
        operatorService.demoteSellerToCustomer(id);
    }
}
