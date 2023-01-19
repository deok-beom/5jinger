package com.sparta.ojinger.repository;

import com.sparta.ojinger.dto.ItemResponseDto;
import com.sparta.ojinger.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
