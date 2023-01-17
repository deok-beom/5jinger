package com.sparta.ojinger.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Seller {

    @Id
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String intro;

    @Column(nullable = false)
    private String category;

    public Seller(User user) {
        this.user = user;
    }
}
