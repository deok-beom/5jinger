package com.sparta.ojinger.service;

import com.sparta.ojinger.entity.ProcessStatus;
import com.sparta.ojinger.entity.Seller;
import com.sparta.ojinger.entity.User;
import com.sparta.ojinger.entity.UserRoleEnum;
import com.sparta.ojinger.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static com.sparta.ojinger.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class OperatorService {
    private final SellerService sellerService;
    private final UserService userService;
    private final PromotionRequestService promotionRequestService;

    @Transactional
    public void promoteCustomerToSeller(Long requestId) {
        Long userId = promotionRequestService.updatePromotionRequestStatus(requestId, ProcessStatus.APPROVED);

        User user;
        try {
            user = userService.updateCustomerRole(userId, UserRoleEnum.SELLER);
        } catch (IllegalArgumentException e) {
            throw new CustomException(IMPROPER_PROMOTION);
        } catch (EntityNotFoundException e){
            throw new CustomException(USER_NOT_FOUND);
        }

        try {
            sellerService.createSeller(user);
        } catch (DuplicateKeyException e) {
            throw new CustomException(DUPLICATE_SELLER);
        }
    }

    @Transactional
    public void demoteSellerToCustomer(Long sellerId) {
        Seller seller = sellerService.getSellerByUserId(sellerId);
        User user = userService.updateCustomerRole(seller.getUser().getId(), UserRoleEnum.CUSTOMER);
        sellerService.deleteSeller(user);
    }
}
