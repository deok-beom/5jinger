package com.sparta.ojinger.controller;

import com.sparta.ojinger.dto.CustomerResponseDto;
import com.sparta.ojinger.dto.ElevationResponseDto;
import com.sparta.ojinger.dto.SellerResponseDto;
import com.sparta.ojinger.entity.ElevationStatus;
import com.sparta.ojinger.entity.User;
import com.sparta.ojinger.entity.UserRoleEnum;
import com.sparta.ojinger.service.ElevationService;
import com.sparta.ojinger.service.SellerService;
import com.sparta.ojinger.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/operators")
public class OperatorController {
    private final UserService userService;
    private final SellerService sellerService;
    private final ElevationService elevationService;

    @GetMapping("/customers")
    public List<CustomerResponseDto> getAllCustomers(@PageableDefault(size = 5) Pageable pageable) {
        return userService.getAllCustomers(pageable);
    }

    @GetMapping("/sellers")
    public List<SellerResponseDto> getAllSellers(@PageableDefault(size = 5) Pageable pageable) {
        return sellerService.getAllSellers(pageable);
    }

    @GetMapping("/sellers/elevations")
    public List<ElevationResponseDto> getAllElevationRequests(@PageableDefault(size = 5) Pageable pageable) {
        return elevationService.getAllElevationRequests(pageable);
    }

    @PostMapping("/sellers/{id}/elevations")
    public void approveElevationRequest(@PathVariable Long id) {
        elevationService.updateElevationStatus(id, ElevationStatus.APPROVED);

        User user = null;
        try {
            user = userService.updateCustomerRole(id, UserRoleEnum.SELLER);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            elevationService.updateElevationStatus(id, ElevationStatus.PENDING);
        }

        try {
            sellerService.createSeller(user);
        } catch (DuplicateKeyException e) {
            elevationService.updateElevationStatus(id, ElevationStatus.PENDING);
            userService.updateCustomerRole(id, UserRoleEnum.CUSTOMER);
        }

    }

    @PatchMapping("/sellers/{id}/elevations")
    public void rejectElevationRequest(@PathVariable Long id) {
        elevationService.updateElevationStatus(id, ElevationStatus.REJECTED);
    }

    @PatchMapping("/seller/{id}/demotion")
    public void demoteSellerToCustomer(@PathVariable Long id) {
        User user = userService.updateCustomerRole(id, UserRoleEnum.CUSTOMER);

        try {
            sellerService.deleteSeller(user);
        } catch (DuplicateKeyException e) {
            userService.updateCustomerRole(id, UserRoleEnum.SELLER);
        }
    }
}
