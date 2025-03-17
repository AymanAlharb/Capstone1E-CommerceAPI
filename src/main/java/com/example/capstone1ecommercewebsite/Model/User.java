package com.example.capstone1ecommercewebsite.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class User {
    @NotEmpty(message = "The id can not be empty.")
    private String id;
    @NotEmpty(message = "The user name can not be empty.")
    @Size(min = 5, message = "The user name must be more than three character long.")
    @Size(max = 19, message = "The user name must be less than 19 character long.")
    private String user_name;
    @NotEmpty(message = "The password can not be empty.")
    @Size(min = 6, message = "The password must be more than 6 character long.")
    private String password;
    @Email
    private String email;
    @NotEmpty(message = "The role can not be empty.")
    @Pattern(regexp = "Admin|Customer|admin|customer")
    private String role;
    @NotNull(message = "The balance can not be null.")
    @PositiveOrZero(message = "The balance can not be negative.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private double balance;
    @Size(max = 0, message = "Cart must be empty.")
    @NotNull(message = "The shopping cart can not be null.")
    ArrayList<String> shopping_cart;
    @Size(max = 0, message = "acquired products list must be empty.")
    @NotNull(message = "The acquired products list can not be null.")
    ArrayList<String> acquired_products_list;
    @NotNull(message = "The discount checker can not be null.")
    private boolean hasDiscount;
    private double discountAmount;

}

