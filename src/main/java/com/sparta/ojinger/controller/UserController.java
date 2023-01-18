package com.sparta.ojinger.controller;

import com.sparta.ojinger.dto.UserDto;
import com.sparta.ojinger.entity.User;
import com.sparta.ojinger.entity.UserRoleEnum;
import com.sparta.ojinger.exception.CustomException;
import com.sparta.ojinger.jwt.JwtUtil;
import com.sparta.ojinger.repository.UserRepository;
import com.sparta.ojinger.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.sparta.ojinger.exception.ErrorCode.ADMIN_PASSWORD_NOT_FOUND;
import static com.sparta.ojinger.exception.ErrorCode.DUPLICATE_USERNAME;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody @Validated UserDto.signUpRequestDto signUpRequestDto, BindingResult bindingresult) {

        System.out.println("테스트");
        //유효성 검사 실패할 경우 에러메세지 반환
        if (bindingresult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingresult.getAllErrors().toString());
        }

        userService.signUp(signUpRequestDto);
        return new ResponseEntity<>("회원가입에 성공하였습니다", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestParam String username, @RequestParam String password, HttpServletResponse response){

        UserDto.loginResponseDto user = userService.login(username, password);

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
        return new ResponseEntity("로그인에 성공하였습니다.", HttpStatus.OK);
    }
}