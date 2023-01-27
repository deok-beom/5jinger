package com.sparta.ojinger.repository;

import com.sparta.ojinger.entity.CustomerRequest;
import com.sparta.ojinger.entity.ProcessStatus;
import com.sparta.ojinger.entity.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRequestRepository extends JpaRepository<CustomerRequest,Long> {

    Optional<CustomerRequest> findByUserIdAndItemIdAndStatus(Long userId, Long itemId, ProcessStatus status);
    Page<CustomerRequest> findAllBySellerId(Long sellerId, Pageable pageable);
    Page<CustomerRequest> findAllByUserId(Long userId, Pageable pageable);
    Page<CustomerRequest> findAllByUserIdAndItemId(Long userId, Long itemId, Pageable pageable);
    @Modifying
    @Query("update CustomerRequest c set c.status = 'CANCELED' WHERE c.sellerId = :sellerId AND c.status = 'PENDING'")
    void canceledAllRequestsToSeller(@Param("sellerId") Long sellerId);

    @Modifying
    @Query("update CustomerRequest c set c.status = 'CANCELED' WHERE c.itemId = :itemId AND c.status = 'PENDING'")
    void canceledAllRequestsAboutItem(@Param("itemId") Long itemId);

}
