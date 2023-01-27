package com.sparta.ojinger.repository;

import com.sparta.ojinger.entity.PromotionRequest;
import com.sparta.ojinger.entity.ProcessStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromotionRequestRepository extends JpaRepository<PromotionRequest, Long> {
    Optional<PromotionRequest> findByUserIdAndStatus(Long id, ProcessStatus status);
}
