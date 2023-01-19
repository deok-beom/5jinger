package com.sparta.ojinger.controller;

import com.sparta.ojinger.dto.ItemResponseDto;
import com.sparta.ojinger.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items")
    public List<ItemResponseDto> getAllItems(@RequestParam int page) {
        return itemService.getAllItem(PageConfig.pageableSetting(page));
    }

    @GetMapping("/items/{id}")
    public ItemResponseDto getItemById(@PathVariable Long id) {
        return itemService.getItemById(id);
    }
}
