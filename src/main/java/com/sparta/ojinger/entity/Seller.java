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

    @Column(nullable = true)
    @Setter
    private String intro;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Category> categories;

    public void setUser(User user) {
        this.user = user;
    }

    public void addCategory(List<Category> categories) {
        for (Category category : categories) {
            this.categories.add(category);
        }
    }

    public String getCategoriesToString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.categories.size(); i++) {
            sb.append(categories.get(i).getCategoryName());
            if (i != this.categories.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
