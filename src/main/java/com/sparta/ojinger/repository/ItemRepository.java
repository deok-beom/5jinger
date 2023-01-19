package com.sparta.ojinger.repository;

import com.sparta.ojinger.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
