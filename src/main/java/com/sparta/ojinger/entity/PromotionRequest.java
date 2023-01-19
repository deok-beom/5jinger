package com.sparta.ojinger.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class PromotionRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    @Setter
    @Enumerated(EnumType.STRING)
    private ProcessStatus status;

    @CreatedDate
    private LocalDateTime requestDate;

//    public void updateStatus(ElevationStatus status) {
//        this.status = status;
//    }
//
//    public Elevation(User user, ElevationStatus status) {
//        this.user = user;
//        this.status = status;
//        this.requestDate = LocalDateTime.now();
//    }
}