package com.sparta.ojinger.repository;

import com.sparta.ojinger.entity.CustomerRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRequestRepository extends JpaRepository<CustomerRequest,Long> {
    Optional<CustomerRequest> findByIdAndItemId(Long id, Long itemId);
    Page<CustomerRequest> findAllBySellerId(Long id, Pageable pageable);

}
