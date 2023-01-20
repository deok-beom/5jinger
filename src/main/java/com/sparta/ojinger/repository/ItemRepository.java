package com.sparta.ojinger.repository;

import com.sparta.ojinger.entity.Item;
import com.sparta.ojinger.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Modifying
    @Query("update Item i set i.status = 'SUSPENSION' WHERE i.seller = :seller")
    void suspendAllItemBySeller(@Param("seller") Seller seller);
}
