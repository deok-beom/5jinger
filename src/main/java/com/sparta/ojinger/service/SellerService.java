package com.sparta.ojinger.service;

import com.sparta.ojinger.dto.seller.SellerProfileResponseDto;
import com.sparta.ojinger.dto.operator.SellerResponseDto;
import com.sparta.ojinger.dto.seller.SellerProfileRequestDto;
import com.sparta.ojinger.entity.Category;
import com.sparta.ojinger.entity.Seller;
import com.sparta.ojinger.entity.User;
import com.sparta.ojinger.exception.CustomException;
import com.sparta.ojinger.exception.ErrorCode;
import com.sparta.ojinger.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sparta.ojinger.exception.ErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class SellerService {
    private final SellerRepository sellerRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<SellerProfileResponseDto> getSellerProfiles(Pageable pageable) {
        Page<Seller> sellersPage = sellerRepository.findAllByAvailableTrue(pageable);
        if (sellersPage.isEmpty()) {
            throw new CustomException(ErrorCode.PAGINATION_IS_NOT_EXIST);
        }

        List<SellerProfileResponseDto> sellerProfileResponseDtoList = new ArrayList<>();
        for (Seller seller : sellersPage) {
            sellerProfileResponseDtoList.add(new SellerProfileResponseDto(seller));
        }
        return sellerProfileResponseDtoList;
    }

    @Transactional(readOnly = true)
    public SellerProfileResponseDto getSellerProfileById(Long id) {
        Seller seller = sellerRepository.findByUserIdAndAvailableTrue(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return new SellerProfileResponseDto(seller);
    }

    @Transactional(readOnly = true)
    public List<SellerResponseDto> getSellersForOperator(Pageable pageable) {
        List<SellerResponseDto> responseDtoList = new ArrayList<>();
        Page<Seller> sellers = sellerRepository.findAll(pageable);

        for (Seller seller : sellers) {
            SellerResponseDto responseDto = new SellerResponseDto(seller);
            responseDtoList.add(responseDto);
        }

        return responseDtoList;
    }

    @Transactional
    public void createSeller(User user) {
        Optional<Seller> optionalSeller = sellerRepository.findByUser(user);
        if (optionalSeller.isPresent()) {
            Seller seller = optionalSeller.get();
            if (seller.isAvailable()) {
                throw new DuplicateKeyException("이미 존재하는 판매자입니다.");
            } else {
                seller.setAvailable(true);
                return;
            }
        }

        Seller seller = new Seller();
        seller.setUser(user);
        sellerRepository.save(seller);
    }

    @Transactional
    public Seller unavailableSeller(User user) {
        Optional<Seller> optionalSeller = sellerRepository.findByUserAndAvailableTrue(user);
        if (optionalSeller.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Seller seller = optionalSeller.get();
        seller.setAvailable(false);
        return seller;
    }

    @Transactional
    public void updateMySellerProfile(SellerProfileRequestDto requestDto, List<Category> categories, User user){
        // 현재 사용자의 판매자 정보를 불러온다.
        Seller seller = getSellerByUserId(user.getId());

        // 닉네임, 이미지에 대한 업데이트를 수행한다.
        userService.updateMyProfile(requestDto, user.getUsername());

        // 소개(Intro) 정보가 공백이면 업데이트를 수행하지 않는다.
        if (!requestDto.getIntro().trim().equals("")) {
            seller.getCategories().clear();
            seller.setIntro(requestDto.getIntro());
        }

        // 카테고리(Category) 정보가 없으면 업데이트를 수행하지 않는다.
        if (categories.size() != 0) {
            seller.addCategory(categories);
        }
    }

    @Transactional(readOnly = true)
    public SellerProfileResponseDto getMySellerProfileResponseDto(Long userId){
        Seller seller = getSellerByUserId(userId);
        return new SellerProfileResponseDto(seller);
    }

    @Transactional(readOnly = true)
    public Seller getSellerByUserId(Long id) {
        Seller seller = sellerRepository.findByUserIdAndAvailableTrue(id).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
        return seller;
    }

    @Transactional(readOnly = true)
    public Seller getSellerById2(Long id) {
        Seller seller = sellerRepository.findById(id).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
        return seller;
    }
}
