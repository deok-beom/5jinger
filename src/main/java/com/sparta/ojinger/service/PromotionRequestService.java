package com.sparta.ojinger.service;

import com.sparta.ojinger.dto.PromotionRequestResponseDto;
import com.sparta.ojinger.entity.PromotionRequest;
import com.sparta.ojinger.entity.ProcessStatus;
import com.sparta.ojinger.entity.User;
import com.sparta.ojinger.exception.CustomException;
import com.sparta.ojinger.exception.ErrorCode;
import com.sparta.ojinger.repository.PromotionRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PromotionRequestService {
    private final PromotionRequestRepository promotionRequestRepository;

    @Transactional
    public void requestPromotion(User customer) {
        // 사용자가 이미 등업 요청한 것이 있는지 확인한다. 이 때 요청은 처리되지 않은 것(PENDING 상태)만 확인한다.
        Optional<PromotionRequest> optionalRequest = promotionRequestRepository.findByUserIdAndStatus(customer.getId(), ProcessStatus.PENDING);
        if (optionalRequest.isPresent()) {
            throw new CustomException(ErrorCode.REQUEST_IS_EXIST);
        }

        PromotionRequest request = new PromotionRequest(customer, ProcessStatus.PENDING);
        promotionRequestRepository.save(request);
    }

    @Transactional(readOnly = true)
    public List<PromotionRequestResponseDto> getAllPromotionRequests(Pageable pageable) {
        List<PromotionRequestResponseDto> responseDtoList = new ArrayList<>();
        Page<PromotionRequest> promotions = promotionRequestRepository.findAll(pageable);

        for (PromotionRequest request : promotions) {
            PromotionRequestResponseDto responseDto = new PromotionRequestResponseDto(request);
            responseDtoList.add(responseDto);
        }

        return responseDtoList;
    }

    @Transactional
    public Long updatePromotionRequestStatus(Long requestId, ProcessStatus status) {
        // ID를 이용해 요청을 찾는다.
        Optional<PromotionRequest> optionalPromotionRequest = promotionRequestRepository.findById(requestId);
        if (optionalPromotionRequest.isEmpty()) {
            throw new CustomException(ErrorCode.ENTITY_NOT_FOUND);
        }

        PromotionRequest request = optionalPromotionRequest.get();
        // 요청의 상태가 처리되지 않은 상태(PENDING 상태)인지 검증한다.
        if (request.getStatus().equals(ProcessStatus.PENDING)) {
            request.setStatus(status);
            return request.getUser().getId();
        } else {
            throw new CustomException(ErrorCode.REQUEST_IS_EXPIRED);
        }
    }
}
