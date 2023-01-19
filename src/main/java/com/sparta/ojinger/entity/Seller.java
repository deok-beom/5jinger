package com.sparta.ojinger.entity;

import com.sparta.ojinger.dto.SellerProfileRequestDto;
import com.sparta.ojinger.dto.SellerProfileResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(fetch = FetchType.LAZY)
    private List<Category> categories;

    public void updateProfile(String intro, String nickname, String image) {
        this.intro = intro;
        this.user.updateUserProfile(nickname, image);
    }

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
