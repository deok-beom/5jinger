package com.sparta.ojinger.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Generated;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Item extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private String category;
    @Column(nullable = false)
    private Long price;

    @ManyToOne
    private User user;

    public Item(String title, String content, String category, Long price) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.price = price;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
