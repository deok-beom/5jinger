package com.sparta.ojinger.controller;

import com.sparta.ojinger.dto.CustomerResponseDto;
import com.sparta.ojinger.dto.ElevationRequestResponseDto;
import com.sparta.ojinger.dto.SellerResponseDto;
import com.sparta.ojinger.entity.ElevationStatus;
import com.sparta.ojinger.entity.User;
import com.sparta.ojinger.entity.UserRoleEnum;
import com.sparta.ojinger.exception.CustomException;
import com.sparta.ojinger.service.ElevationRequestService;
import com.sparta.ojinger.service.SellerService;
import com.sparta.ojinger.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static com.sparta.ojinger.exception.ErrorCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/operators")
public class OperatorController {
    private final UserService userService;
    private final SellerService sellerService;
    private final ElevationRequestService elevationRequestService;

    @GetMapping("/customers")
    public List<CustomerResponseDto> getAllCustomers(@PageableDefault(size = 5) Pageable pageable) {
        return userService.getAllCustomers(pageable);
    }

    @GetMapping("/sellers")
    public List<SellerResponseDto> getAllSellers(@PageableDefault(size = 5) Pageable pageable) {
        return sellerService.getAllSellers(pageable);
    }

    @GetMapping("/sellers/elevations")
    public List<ElevationRequestResponseDto> getAllElevationRequests(@PageableDefault(size = 5) Pageable pageable) {
        return elevationRequestService.getAllElevationRequests(pageable);
    }

    @PostMapping("/elevations/{id}")
    public ResponseEntity approveElevationRequest(@PathVariable Long id) {
        elevationRequestService.updateElevationRequestStatus(id, ElevationStatus.APPROVED);

        User user;
        try {
            user = userService.updateCustomerRole(id, UserRoleEnum.SELLER);
        } catch (IllegalArgumentException e) {
            elevationRequestService.updateElevationRequestStatus(id, ElevationStatus.PENDING);
            throw new CustomException(IMPROPER_ELEVATION);
        } catch (EntityNotFoundException e){
            elevationRequestService.updateElevationRequestStatus(id, ElevationStatus.PENDING);
            throw new CustomException(USER_NOT_FOUND);
        }

        try {
            sellerService.createSeller(user);
        } catch (DuplicateKeyException e) {
            elevationRequestService.updateElevationRequestStatus(id, ElevationStatus.PENDING);
            userService.updateCustomerRole(id, UserRoleEnum.CUSTOMER);
            throw new CustomException(DUPLICATE_SELLER);
        }

        return new ResponseEntity<>("성공하였습니다.", HttpStatus.OK);
    }

    @PatchMapping("/sellers/{id}/elevations")
    public void rejectElevationRequest(@PathVariable Long id) {
        elevationRequestService.updateElevationRequestStatus(id, ElevationStatus.REJECTED);
    }

    @PatchMapping("/seller/{id}/demotion")
    public void demoteSellerToCustomer(@PathVariable Long userId) {
        User user = userService.updateCustomerRole(userId, UserRoleEnum.CUSTOMER);

        try {
            userService.updateCustomerRole(userId, UserRoleEnum.SELLER);
            sellerService.deleteSeller(user);
        } catch (DuplicateKeyException e) {
            throw new CustomException(ENTITY_NOT_FOUND);
        }
    }
}
