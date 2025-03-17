package com.example.capstone1ecommercewebsite.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Category{
    @NotEmpty(message = "The id can not be empty.")
    private String id;
    @NotEmpty(message = "The name can not be empty.")
    @Size(min = 4, message = "The name must be more than three character long.")
    private String name;
}
