package com.sparta.ojinger.controller;

import com.sparta.ojinger.dto.ItemResponseDto;
import com.sparta.ojinger.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items")
    public List<ItemResponseDto> getAllItems() {
        return itemService.getAllItem();
    }

    @GetMapping("/items/{id}")
    public ItemResponseDto getItemById(@PathVariable Long id) {
        return itemService.getItemById(id);
    }
}
