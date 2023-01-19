package com.sparta.ojinger.service;



import com.sparta.ojinger.dto.CustomerResponseDto;
import com.sparta.ojinger.dto.UserDto;
import com.sparta.ojinger.dto.customer.CustomerProfileRequestDto;
import com.sparta.ojinger.dto.customer.CustomerProfileResponseDto;
import com.sparta.ojinger.entity.ProcessStatus;
import com.sparta.ojinger.entity.PromotionRequest;
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
        Optional<User> check_result = userRepository.findByUsername(signUpDto.getUsername());
        check_result.ifPresent(m -> {
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
    public UserDto.loginResponseDto login(String username, String password) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(PASSWORD_NOT_FOUND);
        }
        return new UserDto.loginResponseDto(user.getUsername(), user.getPassword(), user.getRole());
    }

    @Transactional(readOnly = true)
    public List<CustomerResponseDto> getAllCustomers(Pageable pageable) {
        List<CustomerResponseDto> responseDtoList = new ArrayList<>();
        Page<User> customers = userRepository.findAllByRole(UserRoleEnum.CUSTOMER, pageable);

        for (User customer : customers) {
            CustomerResponseDto responseDto = new CustomerResponseDto(customer.getId(), customer.getUsername(),
                    customer.getNickname(), customer.getImage(), customer.getSignUpDate());
            responseDtoList.add(responseDto);
        }

        return responseDtoList;
    }

    @Transactional
    public User updateCustomerRole(Long userId, UserRoleEnum role) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException();
        }

        User user = optionalUser.get();
        if (user.getRole().equals(role) || user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new IllegalArgumentException();
        }

        user.setRole(role);
        return userRepository.save(user);
    }

    //프로필 설정
    @Transactional
    public void updateProfile(CustomerProfileRequestDto customerProfileRequestDto, User user) {
        User customer = userRepository.findById(user.getId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        customer.updateUserProfile(customerProfileRequestDto.getNickName(), customerProfileRequestDto.getImage());
        userRepository.save(customer);
    }

    //프로필 조회
    @Transactional
    public CustomerProfileResponseDto lookUpProfile(User user) {
        return new CustomerProfileResponseDto(getUserById(user.getId()));
    }

    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
