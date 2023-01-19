package com.sparta.ojinger.repository;

import com.sparta.ojinger.entity.ElevationRequest;
import com.sparta.ojinger.entity.ElevationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ElevationRequestRepository extends JpaRepository<ElevationRequest, Long> {
    Page<ElevationRequest> findAllByStatus(ElevationStatus status, Pageable pageable);

    Optional<ElevationRequest> findByUserId(Long id);
}
