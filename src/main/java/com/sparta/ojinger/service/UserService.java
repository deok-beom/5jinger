package com.sparta.ojinger.service;


import com.sparta.ojinger.dto.operator.CustomerResponseDto;
import com.sparta.ojinger.dto.UserDto;
import com.sparta.ojinger.dto.CustomerProfileRequestDto;
import com.sparta.ojinger.dto.CustomerProfileResponseDto;
import com.sparta.ojinger.entity.User;
import com.sparta.ojinger.entity.UserRoleEnum;
import com.sparta.ojinger.exception.CustomException;
import com.sparta.ojinger.exception.ErrorCode;
import com.sparta.ojinger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sparta.ojinger.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.token}")
    private String ADMIN_TOKEN;

    @Transactional
    public void signUp(UserDto.signUpRequestDto signUpDto) {
        String password = passwordEncoder.encode(signUpDto.getPassword());
        Optional<User> checkResult = userRepository.findByUsername(signUpDto.getUsername());
        checkResult.ifPresent(m -> {
            throw new CustomException(DUPLICATE_USERNAME);
        });

        UserRoleEnum role = UserRoleEnum.CUSTOMER;
        if (signUpDto.isAdmin()) {
            if (!signUpDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new CustomException(ADMIN_PASSWORD_NOT_FOUND);
            }
            role = UserRoleEnum.ADMIN;
        }
        userRepository.save(new User(signUpDto.getUsername(), password, role));
    }

    @Transactional(readOnly = true)
    public UserDto.logInResponseDto logIn(String username, String password) {
        User user = getUserByName(username);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(PASSWORD_NOT_MATCH);
        }

        return new UserDto.logInResponseDto(user.getUsername(), user.getPassword(), user.getRole());
    }

    @Transactional
    public void updateMyProfile(CustomerProfileRequestDto requestDto, String username) {
        User user = getUserByName(username);

        if (!requestDto.getNickname().trim().equals("")) {

            if (userRepository.countByNickname(requestDto.getNickname()) > 0) {
                throw new CustomException(DUPLICATE_NICKNAME);
            } else {
                user.setNickname(requestDto.getNickname());
            }
        }

        if (!requestDto.getImage().trim().equals("")) {
            user.setImage(requestDto.getImage());
        }
    }

    @Transactional(readOnly = true)
    public CustomerProfileResponseDto getMyProfile(String username) {
        return new CustomerProfileResponseDto(getUserByName(username));
    }

    @Transactional(readOnly = true)
    public List<CustomerResponseDto> getAllCustomers(Pageable pageable) {
        List<CustomerResponseDto> responseDtoList = new ArrayList<>();
        Page<User> customers = userRepository.findAllByRole(UserRoleEnum.CUSTOMER, pageable);

        for (User customer : customers) {
            CustomerResponseDto responseDto = new CustomerResponseDto(customer);
            responseDtoList.add(responseDto);
        }

        return responseDtoList;
    }

    @Transactional
    public User updateCustomerRole(Long userId, UserRoleEnum role) {
        // ID를 이용해 권한을 변경할 사용자 정보를 찾는다.
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException();
        }

        User user = optionalUser.get();
        // 권한을 변경할 사용자의 권한이 이미 변경하려는 권한과 같거나, 어드민인 건 아닌지 확인한다.
        if (user.getRole().equals(role) || user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new IllegalArgumentException();
        }

        user.setRole(role);
        return user;
    }

    @Transactional(readOnly = true)
    public User getUserByName(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
