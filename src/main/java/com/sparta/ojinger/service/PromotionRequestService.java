package com.sparta.ojinger.service;

import com.sparta.ojinger.dto.PromotionRequestResponseDto;
import com.sparta.ojinger.entity.PromotionRequest;
import com.sparta.ojinger.entity.ProcessStatus;
import com.sparta.ojinger.repository.PromotionRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PromotionRequestService {
    private final PromotionRequestRepository promotionRequestRepository;

    @Transactional(readOnly = true)
    public List<PromotionRequestResponseDto> getAllPromotionRequests(Pageable pageable) {
        List<PromotionRequestResponseDto> responseDtoList = new ArrayList<>();
        Page<PromotionRequest> promotions = promotionRequestRepository.findAll(pageable);

        for (PromotionRequest promotionRequest : promotions) {
            PromotionRequestResponseDto responseDto = new PromotionRequestResponseDto(promotionRequest.getId(),
                    promotionRequest.getUser().getId(), promotionRequest.getRequestDate(), promotionRequest.getStatus());
            responseDtoList.add(responseDto);
        }

        return responseDtoList;
    }

    @Transactional
    public Long updatePromotionRequestStatus(Long requestId, ProcessStatus status) {
        Optional<PromotionRequest> optionalPromotionRequest = promotionRequestRepository.findById(requestId);
        if (optionalPromotionRequest.isEmpty()) {
            throw new EntityNotFoundException();
        }

        PromotionRequest promotionRequest = optionalPromotionRequest.get();
        if (promotionRequest.getStatus().equals(ProcessStatus.PENDING)) {
            promotionRequest.setStatus(status);
            promotionRequest = promotionRequestRepository.save(promotionRequest);
            return promotionRequest.getUser().getId();
        } else {
            throw new IllegalArgumentException();
        }
    }
}
