package com.sparta.ojinger.repository;

import com.sparta.ojinger.entity.CustomerRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRequestRepository extends JpaRepository<CustomerRequest,Long> {
}
