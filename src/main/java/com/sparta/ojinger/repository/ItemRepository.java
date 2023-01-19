package com.sparta.ojinger.repository;

import com.sparta.ojinger.dto.ItemResponseDto;
import com.sparta.ojinger.entity.Item;
import com.sparta.ojinger.entity.TradeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
