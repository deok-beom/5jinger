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
        Seller seller = sellerRepository.findByIdAndAvailableTrue(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
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
                throw new DuplicateKeyException("?????? ???????????? ??????????????????.");
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
        // ?????? ???????????? ????????? ????????? ????????????.
        Seller seller = getSellerByUserId(user.getId());

        // ?????????, ???????????? ?????? ??????????????? ????????????.
        userService.updateMyProfile(requestDto, user.getUsername());

        // ??????(Intro) ????????? ???????????? ??????????????? ???????????? ?????????.
        if (!requestDto.getIntro().trim().equals("")) {
            seller.getCategories().clear();
            seller.setIntro(requestDto.getIntro());
        }

        // ????????????(Category) ????????? ????????? ??????????????? ???????????? ?????????.
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
        return sellerRepository.findByIdAndAvailableTrue(id).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
    }
}
