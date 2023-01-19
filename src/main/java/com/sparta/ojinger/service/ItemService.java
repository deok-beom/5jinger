package com.sparta.ojinger.service;

import com.sparta.ojinger.dto.seller.ItemRequestDto;
import com.sparta.ojinger.dto.ItemResponseDto;
import com.sparta.ojinger.entity.*;
import com.sparta.ojinger.exception.CustomException;
import com.sparta.ojinger.exception.ErrorCode;
import com.sparta.ojinger.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    public final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    public List<ItemResponseDto> getAllItem(Pageable pageable) {
        Page<Item> items = itemRepository.findAll(pageable);
        List<ItemResponseDto> itemResponseDtoList = new ArrayList<>();
        for (Item item : items) {
            ItemResponseDto itemResponseDto = new ItemResponseDto(item);
            itemResponseDtoList.add(itemResponseDto);
        }

        return itemResponseDtoList;
    }

    @Transactional(readOnly = true)
    public ItemResponseDto getItemById(Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isEmpty()) {
            throw new CustomException(ErrorCode.ENTITY_NOT_FOUND);
        }
        Item item = optionalItem.get();
        return new ItemResponseDto(item);
    }

    @Transactional
    public void addItem(ItemRequestDto requestDto, Seller seller) {
        Item item = new Item(requestDto.getTitle(), requestDto.getCategory(), requestDto.getContent(), requestDto.getPrice());
        item.setSeller(seller);
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(ItemRequestDto requestDto, Long itemId, Long userId) {
        Item item = findItemAndValidUpdatable(itemId);

        // 얻어온 아이템이 현재 요청한 유저의 아이템인지 확인한다.
        if (item.getSeller().getId() != userId) {
            throw new CustomException(ErrorCode.INVALID_AUTH_TOKEN);
        }

        // 제목이 공백이 아니면 업데이트 해준다.
        if (!requestDto.getTitle().trim().equals("")) {
            item.setTitle(requestDto.getTitle());
        }

        // 내용이 공백이 아니면 업데이트 해준다.
        if (!requestDto.getContent().trim().equals("")) {
            item.setContent(requestDto.getContent());
        }

        // 카테고리가 공백이 아니면 업데이트 해준다.
        if (!requestDto.getCategory().trim().equals("")) {
            //item.setCategory(requestDto.getCategory());
        }

        // 가격이 0원이 아니면 업데이트 해준다.
        if (requestDto.getPrice() != 0) {
            item.setPrice(requestDto.getPrice());
        }

        itemRepository.save(item);
    }

    @Transactional
    public void suspendItem(Long itemId, User user) {
        Item item = findItemAndValidUpdatable(itemId);

        // 삭제하려는 아이템을 등록한 사람이 현재 사용자 자신이거나, 현재 사용자의 권한이 관리자라면 삭제를 수행한다.
        if (item.getSeller().getId() == user.getId() || user.getRole().equals(UserRoleEnum.ADMIN)) {
            item.setStatus(TradeStatus.SUSPENSION);
        } else {
            throw new CustomException(ErrorCode.INVALID_AUTH_TOKEN);
        }
    }

    @Transactional(readOnly = true)
    public Item findItemAndValidUpdatable(Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) {
            throw new CustomException(ErrorCode.ENTITY_NOT_FOUND);
        }

        Item item = optionalItem.get();

        // 얻어온 아이템이 현재 수정할 수 없는 상태(status)인지 확인
        if (!item.getStatus().equals(TradeStatus.ON_SALE)) {
            throw new CustomException(ErrorCode.ITEM_IS_EXPIRED);
        }

        return item;
    }
}
