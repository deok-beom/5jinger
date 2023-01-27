package com.sparta.ojinger.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "Users")
@NoArgsConstructor
@Getter
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
    private UserRoleEnum role;

    @Column(nullable = false)
    @Setter
    private String nickname;

    @Column
    @Setter
    private String image;

    public User(String username, String password, UserRoleEnum role){
        this.username = username;
        this.password = password;
        this.nickname = this.username;
        this.image = "기본 이미지";
        this.role = role;
    }
}

