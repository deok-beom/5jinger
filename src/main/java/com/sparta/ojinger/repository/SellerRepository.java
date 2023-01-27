package com.sparta.ojinger.repository;

import com.sparta.ojinger.entity.Seller;
import com.sparta.ojinger.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findByUser(User user);
    Optional<Seller> findByUserAndAvailableTrue(User user);
    Optional<Seller> findByIdAndAvailableTrue(Long id);
    Page<Seller> findAllByAvailableTrue(Pageable pageable);



}
