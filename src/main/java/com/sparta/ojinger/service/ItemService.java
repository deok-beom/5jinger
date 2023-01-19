package com.sparta.ojinger.service;

import com.sparta.ojinger.dto.ItemRequestDto;
import com.sparta.ojinger.dto.ItemResponseDto;
import com.sparta.ojinger.entity.Item;
import com.sparta.ojinger.entity.User;
import com.sparta.ojinger.exception.CustomException;
import com.sparta.ojinger.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public List<ItemResponseDto> getAllItem() {
        List<Item> items = itemRepository.findAll();
        List<ItemResponseDto> itemResponseDtos = new ArrayList<>();
        for (Item item : items) {
            ItemResponseDto itemResponseDto = new ItemResponseDto(item.getId(), item.getTitle(), item.getContent(),
                    item.getCategory(), item.getPrice(), item.getCreateAt(), item.getModifiedAt());
            itemResponseDtos.add(itemResponseDto);
        }

        return itemResponseDtos;
    }

    public ItemResponseDto getItemById(Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Item item = optionalItem.get();
        return new ItemResponseDto(item.getId(), item.getTitle(), item.getContent(),
                item.getCategory(), item.getPrice(), item.getCreateAt(), item.getModifiedAt());
    }

    public void addItem(ItemRequestDto requestDto, User user) {
        Item item = new Item(requestDto.getTitle(), requestDto.getCategory(), requestDto.getContent(), requestDto.getPrice());
        item.setUser(user);
        itemRepository.save(item);
    }
}
