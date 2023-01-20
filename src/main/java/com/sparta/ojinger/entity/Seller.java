package com.sparta.ojinger.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Seller extends Timestamped{
    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    private String intro;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Category> categories;

    @Setter
    private boolean available = true;

    public void setUser(User user) {
        this.user = user;
    }

    public void addCategory(List<Category> categories) {
        CategoriesMethod.addCategories(this.categories, categories);
    }

    public String getCategoriesToString() {
        return CategoriesMethod.categoriesToString(this.categories);
    }
}
