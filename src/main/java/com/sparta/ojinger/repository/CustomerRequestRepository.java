package com.sparta.ojinger.repository;

import com.sparta.ojinger.entity.CustomerRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRequestRepository extends JpaRepository<CustomerRequest,Long> {
    Optional<CustomerRequest> findByIdAndSellerUsername(Long id, String username);
}
