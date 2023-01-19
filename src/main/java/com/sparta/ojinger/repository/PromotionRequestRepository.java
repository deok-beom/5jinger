package com.sparta.ojinger.repository;

import com.sparta.ojinger.entity.PromotionRequest;
import com.sparta.ojinger.entity.ProcessStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromotionRequestRepository extends JpaRepository<PromotionRequest, Long> {
    Page<PromotionRequest> findAllByStatus(ProcessStatus status, Pageable pageable);

    Optional<PromotionRequest> findByUserIdAndStatus(Long id, ProcessStatus status);
}
