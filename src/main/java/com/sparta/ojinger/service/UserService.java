package com.sparta.ojinger.service;



import com.sparta.ojinger.dto.UserDto;
import com.sparta.ojinger.entitiy.User;
import com.sparta.ojinger.entitiy.UserRoleEnum;
import com.sparta.ojinger.exception.CustomException;
import com.sparta.ojinger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.ojinger.exception.ErrorCode.PASSWORD_NOT_FOUND;
import static com.sparta.ojinger.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(UserDto.signUpRequestDto signUpDto, UserRoleEnum role) {
        String password = passwordEncoder.encode(signUpDto.getPassword());
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
}
