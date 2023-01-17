package com.sparta.ojinger.service;

import com.sparta.ojinger.dto.SellerResponseDto;
import com.sparta.ojinger.entity.Seller;
import com.sparta.ojinger.entity.User;
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

@RequiredArgsConstructor
@Service
public class SellerService {
    private final SellerRepository sellerRepository;

    @Transactional(readOnly = true)
    public List<SellerResponseDto> getAllSellers(Pageable pageable) {
        List<SellerResponseDto> responseDtoList = new ArrayList<>();
        Page<Seller> sellers = sellerRepository.findAll(pageable);

        for (Seller seller : sellers) {
            User user = seller.getUser();
            SellerResponseDto responseDto = new SellerResponseDto(user.getId(), user.getUsername(),
                    user.getNickname(), user.getImage(), user.getSignUpDate(), seller.getIntro(), seller.getCategory());
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

        Seller seller = new Seller(user);
        sellerRepository.save(seller);
    }

    public void deleteSeller(User user) {
        Optional<Seller> optionalSeller = sellerRepository.findByUser(user);
        if (optionalSeller.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Seller seller = optionalSeller.get();
        sellerRepository.delete(seller);
    }
}
