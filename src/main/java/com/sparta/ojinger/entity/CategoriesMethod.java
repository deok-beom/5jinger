package com.sparta.ojinger.entity;

import java.util.List;

public class CategoriesMethod {
    public static void addCategories(List<Category> fieldCategories, List<Category> inputCategories) {
        fieldCategories.addAll(inputCategories);
    }

    public static String categoriesToString(List<Category> fieldCategories) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fieldCategories.size(); i++) {
            sb.append(fieldCategories.get(i).getCategoryName());
            if ( i != fieldCategories.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
