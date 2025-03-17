package com.example.capstone1ecommercewebsite.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MerchantStock {
    @NotEmpty(message = "The id can not be empty.")
    private String id;
    @NotEmpty(message = "The product id can not be empty.")
    private String productId;
    @NotEmpty(message = "The merchant id can not be empty.")
    private String merchantId;
    @NotNull
    private int stock;
}
