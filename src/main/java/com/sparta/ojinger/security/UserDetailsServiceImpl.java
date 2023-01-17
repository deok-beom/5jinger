package com.sparta.ojinger.security;

import com.sparta.ojinger.exception.CustomException;
import com.sparta.ojinger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.sparta.ojinger.entitiy.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.sparta.ojinger.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new CustomException(USER_NOT_FOUND));

        return new UserDetailsImpl(user, user.getUsername());
    }

}