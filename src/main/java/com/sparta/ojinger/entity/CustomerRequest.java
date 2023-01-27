package com.sparta.ojinger.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class CustomerRequest {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long itemId;

    @Column(nullable = false)
    private Long sellerId;

    @Column
    private String sellerNickname;

    @Setter
    @Enumerated(EnumType.STRING)
    private ProcessStatus status;

    @CreatedDate
    private LocalDateTime requestDate;


    public CustomerRequest(Long itemId, String message, ProcessStatus status, Long userId, Long sellerId, String sellerNickname) {
        this.itemId = itemId;
        this.message = message;
        this.status = status;
        this.requestDate = LocalDateTime.now();
        this.userId = userId;
        this.sellerId = sellerId;
        this.sellerNickname = sellerNickname;
    }
}
