package com.sparta.ojinger.service;

import com.sparta.ojinger.dto.UserDto;
import com.sparta.ojinger.entitiy.UserRoleEnum;
import com.sparta.ojinger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void signUp(UserDto.signUpRequestDto signUpDto, UserRoleEnum role) {
        String password = passwordEncoder.encode(signUpDto.getPassword());
        userRepository.save(new User(signUpDto.getUsername(), password, role));
    }
}
