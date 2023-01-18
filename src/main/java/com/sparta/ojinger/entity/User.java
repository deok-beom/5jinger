package com.sparta.ojinger.entity;

import com.sparta.ojinger.dto.customer.CustomerProfileRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "Users")
@NoArgsConstructor
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
    private UserRoleEnum role;

    @Column(nullable = false)
    private LocalDateTime signUpDate;

    @Column(nullable = true)
    private String nickname;

    @Column(nullable = true)
    private String image;

    @Column(nullable = true)
    private String catagory;


    public User(String username, String password, UserRoleEnum role){
        this.username = username;
        this.password = password;
        this.role = role;
        this.signUpDate = LocalDateTime.now();
    }

    public void updateUser(CustomerProfileRequestDto customerProfileRequestDto) {
        this.nickname = customerProfileRequestDto.getNickName();
        this.image = customerProfileRequestDto.getImage();
    }
}

