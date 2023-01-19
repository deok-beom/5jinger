package com.sparta.ojinger.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.processing.Generated;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Item extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String title;

    @Setter
    @Column(nullable = false)
    private String content;

    @Setter
    @Column(nullable = false)
    private String category;

    @Setter
    @Column(nullable = false)
    private Long price;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @Setter
    @Enumerated(EnumType.STRING)
    private TradeStatus tradeStatus;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Category> categories;

    public Item(String title, String content, String category, Long price) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.price = price;
        this.tradeStatus = TradeStatus.ON_SALE;
        this.categories = new ArrayList<>();
    }

    public void addCategory(List<Category> categories) {
        CategoriesMethod.addCategories(this.categories, categories);
    }

    public String getCategories() {
        return CategoriesMethod.categoriesToString(this.categories);
    }
}
