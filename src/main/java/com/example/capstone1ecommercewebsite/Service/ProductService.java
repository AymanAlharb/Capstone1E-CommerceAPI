package com.example.capstone1ecommercewebsite.Service;

import com.example.capstone1ecommercewebsite.Model.Category;
import com.example.capstone1ecommercewebsite.Model.Product;
import com.example.capstone1ecommercewebsite.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ProductService {
    ArrayList<Product> products = new ArrayList<>();
    private final CategoryService categoryService;

    public ArrayList<Product> getAll() {
        return products;
    }

    public char createProduct(Product product) {
        Category tempCategory = categoryService.searchCategory(product.getCategoryId());
        if (tempCategory == null) return '0'; //Category not found.
        Product tempProduct = searchProduct(product.getId());
        if (tempProduct != null) return '1'; //Id already exists.
        products.add(product);
        return '2'; //Created successfully.
    }

    public char updateProduct(String id, Product product) {
        Category tempCategory = categoryService.searchCategory(product.getCategoryId());
        if (tempCategory == null) return '3'; //Category not found.
        if (!id.equalsIgnoreCase(product.getId())) return '0'; //Ids does not match.
        Product tempProduct = searchProduct(id);
        if (tempProduct == null) return '1'; //Product not found.
        products.set(products.indexOf(tempProduct), product);
        return '2'; //Updated successfully.
    }

    public boolean deleteProduct(String id) {
        Product tempProduct = searchProduct(id);
        if (tempProduct == null) return false;
        products.remove(tempProduct);
        return true;
    }

    //Get product object by product id.
    public Product searchProduct(String id) {
        for (Product product : products) if (product.getId().equalsIgnoreCase(id)) return product;
        return null;
    }
}
