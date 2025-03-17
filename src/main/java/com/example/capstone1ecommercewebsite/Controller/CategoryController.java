package com.example.capstone1ecommercewebsite.Controller;

import com.example.capstone1ecommercewebsite.Api.ApiResponse;
import com.example.capstone1ecommercewebsite.Model.Category;
import com.example.capstone1ecommercewebsite.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;

    @GetMapping("/get-all")
    public ResponseEntity getAll() {
        return ResponseEntity.status(200).body(service.getAll());
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid Category category, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        if (service.createCategory(category))
            return ResponseEntity.status(200).body(new ApiResponse("Category added successfully."));
        return ResponseEntity.status(400).body(new ApiResponse("Id must be unique"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity add(@PathVariable String id, @RequestBody @Valid Category category, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        char status = service.updateCategory(id, category);
        return switch (status) {
            case '0' -> ResponseEntity.status(400).body(new ApiResponse("Ids must match."));
            case '1' -> ResponseEntity.status(400).body(new ApiResponse("Category not found"));
            default -> ResponseEntity.status(200).body(new ApiResponse("Category updated successfully."));
        };

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        if (service.deleteCategory(id))
            return ResponseEntity.status(200).body(new ApiResponse("Category deleted successfully."));
        return ResponseEntity.status(400).body(new ApiResponse("Category not found"));
    }


}
