package com.sparta.ojinger.controller;

import com.sparta.ojinger.dto.UserDto;
import com.sparta.ojinger.dto.user.CustomerProfileRequestDto;
import com.sparta.ojinger.dto.user.CustomerProfileResponseDto;
import com.sparta.ojinger.jwt.JwtUtil;
import com.sparta.ojinger.security.UserDetailsImpl;
import com.sparta.ojinger.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody UserDto.signUpRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors().toString());
        }

        userService.signUp(requestDto);
        return new ResponseEntity<>("회원가입에 성공하였습니다", HttpStatus.OK);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> logIn(@RequestBody UserDto.logInRequestDto requestDto, HttpServletResponse response) {
        UserDto.logInResponseDto user = userService.logIn(requestDto.getUsername(), requestDto.getPassword());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
        return new ResponseEntity<>("로그인에 성공하였습니다.", HttpStatus.OK);
    }

    // 사용자 프로필(별칭, 이미지) 설정
    @PatchMapping("/profile")
    public ResponseEntity<String> updateMyProfile(@RequestBody CustomerProfileRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updateMyProfile(requestDto, userDetails.getUsername());
        return new ResponseEntity<>("프로필 설정완료", HttpStatus.OK);
    }

    // 사용자 프로필 조회
    @GetMapping("/profile")
    public ResponseEntity<CustomerProfileResponseDto> getMyProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(userService.getMyProfile(userDetails.getUsername()), HttpStatus.OK);
    }
}