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
public class ConvenienceService {
    private final SellerService sellerService;
    private final UserService userService;
    private final ItemService itemService;
    private final PromotionRequestService promotionRequestService;
    private final CustomerRequestService customRequestService;

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

        // ID를 이용해 사용자의 정보를 수정한다.
        User user;
        try {
            user = userService.updateCustomerRole(sellerId, UserRoleEnum.CUSTOMER);
        } catch (EntityNotFoundException e) {
            throw new CustomException(USER_NOT_FOUND);
        } catch (IllegalArgumentException e) {
            throw new CustomException(IMPROPER_DEMOTION);
        }

        // 사용자의 판매자 정보를 비활성화 한다.
        Seller seller = sellerService.unavailableSeller(user);

        // 판매자의 모든 아이템을 판매 중지한다.
        itemService.suspendAllSellersItem(seller);

        // 판매자의 모든 아이템에 대한 요청들을 취소시킨다.
        customRequestService.canceledAllRequestsToSeller(sellerId);
    }

    @Transactional
    public void suspendItem(Long itemId, User user) {
        // 아이템을 판매 중지한다.
        itemService.suspendItem(itemId, user);

        // 해당 아이템에 대한 요청들을 취소한다.
        customRequestService.canceledAllRequestsAboutItem(itemId);
    }
}
