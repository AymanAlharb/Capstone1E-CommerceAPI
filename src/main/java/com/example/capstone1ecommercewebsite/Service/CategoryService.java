package com.example.capstone1ecommercewebsite.Service;

import com.example.capstone1ecommercewebsite.Model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CategoryService {
    ArrayList<Category> categories = new ArrayList<>();

    public ArrayList<Category> getAll() {
        return categories;
    }

    public boolean createCategory(Category category) {
        Category tempCategory = searchCategory(category.getId());
        if (tempCategory != null) return false; //Category already exists.
        categories.add(category);
        return true; //Created successfully.
    }

    public char updateCategory(String id, Category category) {
        if (!id.equalsIgnoreCase(category.getId())) return '0'; //Ids does not match.
        Category tempCategory = searchCategory(id);
        if (tempCategory == null) return '1'; //Category not found.
        categories.set(categories.indexOf(tempCategory), category);
        return '2'; //Updated successfully.
    }

    public boolean deleteCategory(String id) {
        Category tempCategory = searchCategory(id);
        if (tempCategory == null) return false; //Category not found.
        categories.remove(tempCategory);
        return true; //Deleted successfully.
    }

    public Category searchCategory(String id) {
        for (Category category : categories) if (category.getId().equalsIgnoreCase(id)) return category;
        return null;
    }

}
