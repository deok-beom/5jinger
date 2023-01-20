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

    @Column(nullable = true,unique = true)
    @Setter
    private String nickname;

    @Column(nullable = true)
    @Setter
    private String image;

    public User(String username, String password, UserRoleEnum role){
        this.username = username;
        this.password = password;
        this.role = role;
    }


    public void updateUserProfile(String nickname, String image) {
        this.nickname = nickname;
        this.image = image;
    }
}

