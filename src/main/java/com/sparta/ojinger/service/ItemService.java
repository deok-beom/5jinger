package com.sparta.ojinger.service;

import com.sparta.ojinger.dto.ItemRequestDto;
import com.sparta.ojinger.dto.ItemResponseDto;
import com.sparta.ojinger.entity.Item;
import com.sparta.ojinger.entity.Seller;
import com.sparta.ojinger.entity.User;
import com.sparta.ojinger.entity.UserRoleEnum;
import com.sparta.ojinger.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    public final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    public List<ItemResponseDto> getAllItem() {
        List<Item> items = itemRepository.findAll();
        List<ItemResponseDto> itemResponseDtoList = new ArrayList<>();
        for (Item item : items) {
            ItemResponseDto itemResponseDto = new ItemResponseDto(item.getId(), item.getSeller().getId(),
                    item.getSeller().getUser().getNickname(), item.getTitle(), item.getContent(),
                    item.getCategory(), item.getPrice(), item.getCreateAt(), item.getModifiedAt());
            itemResponseDtoList.add(itemResponseDto);
        }

        return itemResponseDtoList;
    }

    @Transactional(readOnly = true)
    public ItemResponseDto getItemById(Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Item item = optionalItem.get();
        return new ItemResponseDto(item.getId(), item.getSeller().getId(), item.getSeller().getUser().getNickname(),
                item.getTitle(), item.getContent(),
                item.getCategory(), item.getPrice(), item.getCreateAt(), item.getModifiedAt());
    }

    @Transactional
    public void addItem(ItemRequestDto requestDto, Seller seller) {
        Item item = new Item(requestDto.getTitle(), requestDto.getCategory(), requestDto.getContent(), requestDto.getPrice());
        item.setSeller(seller);
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(ItemRequestDto requestDto, Long itemId, Long userId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Item item = optionalItem.get();
        // 얻어온 아이템이 현재 요청한 유저의 아이템인지 확인
        if (item.getSeller().getUser().getId() != userId) {
            throw new IllegalArgumentException();
        }

        // requestDto에 실려온 제목이 공백이 아니면 업데이트 해준다.
        if (!requestDto.getTitle().trim().equals("")) {
            item.setTitle(requestDto.getTitle());
        }

        // requestDto에 실려온 내용이 공백이 아니면 업데이트 해준다.
        if (!requestDto.getContent().trim().equals("")) {
            item.setContent(requestDto.getContent());
        }

        // requestDto에 실려온 카테고리가 공백이 아니면 업데이트 해준다.
        if (!requestDto.getCategory().trim().equals("")) {
            item.setCategory(requestDto.getCategory());
        }

        // requestDto에 실려온 가격이 공백이 아니면 업데이트 해준다.
        if (requestDto.getPrice() != 0) {
            item.setPrice(requestDto.getPrice());
        }

        itemRepository.save(item);
    }

    private boolean validNullOrBlank(String s) {
        return !(s == null || s.trim().equals(""));
    }

    @Transactional
    public void deleteItem(Long itemId, User user) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Item item = optionalItem.get();
        // 삭제하려는 아이템을 등록한 사람이 요청한 사람 자기 자신이거나, 요청한 사람의 권한이 관리자라면 삭제를 수행
        if (item.getSeller().getUser().getId() == user.getId() || user.getRole().equals(UserRoleEnum.ADMIN)) {
            itemRepository.delete(item);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
