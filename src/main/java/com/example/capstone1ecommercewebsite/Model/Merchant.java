package com.example.capstone1ecommercewebsite.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class Merchant {
    @NotEmpty(message = "The id can not be empty.")
    private String id;
    @NotEmpty(message = "The name can not be empty.")
    @Size(min = 4, message = "The name must be more than three character long.")
    private String name;
    @Size(max = 0, message = "Sold products list must be empty.")
    @NotNull(message = "The sold products list can not be null.")
    ArrayList<String> sold_products;
    @Max(value = 0, message = "merchant money should be 0.")
    @NotNull(message = "The merchant money can not be null.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private double merchant_total_money;
}
