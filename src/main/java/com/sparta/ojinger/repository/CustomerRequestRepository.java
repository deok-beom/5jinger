package com.sparta.ojinger.repository;

import com.sparta.ojinger.entity.CustomerRequest;
import com.sparta.ojinger.entity.ProcessStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRequestRepository extends JpaRepository<CustomerRequest,Long> {

    Optional<CustomerRequest> findByUserIdAndItemIdAndStatus(Long userId, Long itemId, ProcessStatus status);
    Optional<CustomerRequest> findByUserIdAndItemId(Long userId, Long itemId);
    Page<CustomerRequest> findAllBySellerId(Long sellerId, Pageable pageable);
    Page<CustomerRequest> findAllByUserId(Long userId, Pageable pageable);
    Page<CustomerRequest> findAllByUserIdAndItemId(Long userId, Long itemId, Pageable pageable);

}
