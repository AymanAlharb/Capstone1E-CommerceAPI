package com.example.capstone1ecommercewebsite.Controller;

import com.example.capstone1ecommercewebsite.Api.ApiResponse;
import com.example.capstone1ecommercewebsite.Model.Product;
import com.example.capstone1ecommercewebsite.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @GetMapping("/get-all")
    public ResponseEntity getAll() {
        return ResponseEntity.status(200).body(service.getAll());
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid Product product, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        char status = service.createProduct(product);
        return switch (status) {
            case '0' -> ResponseEntity.status(400).body("Category not found.");
            case '1' -> ResponseEntity.status(400).body("Id must be unique");
            default -> ResponseEntity.status(200).body(new ApiResponse("Product added successfully."));
        };
    }

    @PutMapping("/update/{id}")
    public ResponseEntity add(@PathVariable String id, @RequestBody @Valid Product product, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        char status = service.updateProduct(id, product);
        return switch (status) {
            case '0' -> ResponseEntity.status(400).body(new ApiResponse("Ids must match."));
            case '1' -> ResponseEntity.status(400).body(new ApiResponse("Product not found"));
            case '3' -> ResponseEntity.status(400).body(new ApiResponse("Category not found"));
            default -> ResponseEntity.status(200).body(new ApiResponse("Product updated successfully."));
        };
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        if (service.deleteProduct(id))
            return ResponseEntity.status(200).body(new ApiResponse("Product deleted successfully."));
        return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
    }
}
