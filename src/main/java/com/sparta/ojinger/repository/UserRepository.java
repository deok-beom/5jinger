package com.sparta.ojinger.repository;

import com.sparta.ojinger.entity.User;
import com.sparta.ojinger.entity.UserRoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Long countByNickname(String nickname);
    Page<User> findAllByRole(UserRoleEnum customer, Pageable pageable);
    Page<User> findAll(Pageable pageable);
}
