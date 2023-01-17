package com.sparta.ojinger.entity;

import com.sparta.ojinger.dto.SellerProfileResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = true)
    private String intro;

    @Column(nullable = true)
    private String category;

    public Seller(User user) {
        this.user = user;
    }

    public void profileUpdate(SellerProfileResponseDto responseDto) {
        this.intro = responseDto.getIntro();
        this.category = responseDto.getCategory();
        this.user.userChangeNicknameAndImage(responseDto.getNickname(),responseDto.getImage());


    }
}
