package com.example.capstone1ecommercewebsite.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product{
    @NotEmpty(message = "The id can not be empty.")
    private String id;
    @NotEmpty(message = "The name can not be empty.")
    @Size(min = 4, message = "The name must be more than three character long.")
    private String name;
    @NotNull(message = "The price can not be null")
    @PositiveOrZero(message = "The price can not be a negative number.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private double price;
    @NotEmpty(message = "The category id can not be empty.")
    private String categoryId;
    @NotNull(message = "The returnable variable can not be null.")
    private boolean isReturnable;
}
