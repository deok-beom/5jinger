package com.sparta.ojinger.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "seller_username", nullable = false)
    private String sellerUsername;

    private boolean status;

    @CreatedDate
    private LocalDateTime requestDate;


    public CustomerRequest(String username, String message,boolean status, User user) {
        this.sellerUsername = username;
        this.message = message;
        this.status = status;
        this.requestDate = LocalDateTime.now();
        this.userId = user.getId();
    }

    public void updateCustomerRequestStatus(boolean status){
        this.status = status;
    }
}
