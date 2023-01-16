package com.sparta.ojinger.entitiy;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
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
    private UserRoleEnum role;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String registDate;

}
