package com.sparta.ojinger.entity;

import com.sparta.ojinger.dto.customer.SellerElevationsRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class CustomerRequest {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "request_id")
    private Long id;

    @Column(nullable = false)
    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public CustomerRequest(SellerElevationsRequestDto sellerElevationsRequestDto, User user) {
        this.message = sellerElevationsRequestDto.getMessage();
        this.user = user;
    }
}
