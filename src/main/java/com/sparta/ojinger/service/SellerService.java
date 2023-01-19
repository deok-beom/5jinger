package com.sparta.ojinger.service;

import com.sparta.ojinger.dto.SellerProfileResponseDto;
import com.sparta.ojinger.dto.operator.SellerResponseDto;
import com.sparta.ojinger.dto.seller.SellerProfileRequestDto;
import com.sparta.ojinger.entity.Seller;
import com.sparta.ojinger.entity.User;
import com.sparta.ojinger.exception.CustomException;
import com.sparta.ojinger.exception.ErrorCode;
import com.sparta.ojinger.repository.SellerRepository;
import com.sparta.ojinger.security.UserDetailsImpl;
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
    private final CategoryService categoryService;
    private final UserService userService;

    // 판매자 리스트 조회
    @Transactional(readOnly = true)
    public List<SellerProfileResponseDto> getSellers(Pageable pageable) {
        Page<Seller> sellersPage = sellerRepository.findAll(pageable);
        if (sellersPage.isEmpty()) {
            throw new CustomException(ErrorCode.PAGINATION_IS_NOT_EXIST);
        }

        List<SellerProfileResponseDto> sellerProfileResponseDtoList = new ArrayList<>();
        for (Seller seller : sellersPage) {
            sellerProfileResponseDtoList.add(new SellerProfileResponseDto(seller));
        }
        return sellerProfileResponseDtoList;
    }

    // 특정 판매자 조회
    @Transactional(readOnly = true)
    public SellerProfileResponseDto getSellerById(Long id) {
        Seller seller = sellerRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return new SellerProfileResponseDto(seller);
    }

    //
    @Transactional(readOnly = true)
    public List<SellerResponseDto> getAllSellers(Pageable pageable) {
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
            throw new DuplicateKeyException("이미 존재하는 판매자입니다.");
        }

        Seller seller = new Seller();
        seller.setUser(user);
        sellerRepository.save(seller);
    }

    @Transactional
    public void deleteSeller(User user) {
        Optional<Seller> optionalSeller = sellerRepository.findByUser(user);
        if (optionalSeller.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Seller seller = optionalSeller.get();
        sellerRepository.delete(seller);
    }

    @Transactional
    public void updateMySellerProfile(SellerProfileRequestDto requestDto, UserDetailsImpl userDetails){
        Seller seller = getSellerByUserId(userDetails.getUser().getId());
        userService.updateMyProfile(requestDto, userDetails.getUsername());

        if (!requestDto.getIntro().trim().equals("")) {
            seller.setIntro(requestDto.getIntro());
        }

        if (!requestDto.getCategory().trim().equals("")) {
            // List<Category> categories = categoryService.getCategoryFromString(requestDto.getCategory());
            // seller.addCategory(categories);
        }
    }

    @Transactional(readOnly = true)
    public SellerProfileResponseDto getMySellerProfile(UserDetailsImpl userDetails){
        Seller seller = getSellerByUserId(userDetails.getUser().getId());
        return new SellerProfileResponseDto(seller);
    }

    @Transactional(readOnly = true)
    public Seller getSellerByUserId(Long id) {
        Seller seller = sellerRepository.findByUserId(id).orElseThrow(
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
