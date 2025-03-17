package com.example.capstone1ecommercewebsite.Controller;

import com.example.capstone1ecommercewebsite.Api.ApiResponse;
import com.example.capstone1ecommercewebsite.Model.User;
import com.example.capstone1ecommercewebsite.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/get-all")
    public ResponseEntity getAll() {
        return ResponseEntity.status(200).body(service.getAll());
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid User user, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        if (service.createUser(user))
            return ResponseEntity.status(200).body(new ApiResponse("User added successfully."));
        return ResponseEntity.status(400).body(new ApiResponse("Id must be unique"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity add(@PathVariable String id, @RequestBody @Valid User user, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        char status = service.updateUser(id, user);
        return switch (status) {
            case '0' -> ResponseEntity.status(400).body(new ApiResponse("Ids must match."));
            case '1' -> ResponseEntity.status(400).body(new ApiResponse("User not found."));
            default -> ResponseEntity.status(200).body(new ApiResponse("User updated successfully."));
        };
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        if (service.deleteUser(id))
            return ResponseEntity.status(200).body(new ApiResponse("User deleted successfully."));
        return ResponseEntity.status(400).body(new ApiResponse("User not found"));
    }

    @PutMapping("/add-to-cart/{userId}/{productId}/{merchantId}")
    public ResponseEntity addToCart(@PathVariable String userId, @PathVariable String productId, @PathVariable String merchantId) {
        char status = service.addToCart(userId, productId, merchantId);
        return switch (status) {
            case '0' -> ResponseEntity.status(400).body(new ApiResponse("User not found."));
            case '1' -> ResponseEntity.status(400).body(new ApiResponse("Product not found."));
            case '2' -> ResponseEntity.status(400).body(new ApiResponse("Merchant not found."));
            case '3' -> ResponseEntity.status(400).body(new ApiResponse("MerchantStock not found."));
            case '4' -> ResponseEntity.status(400).body(new ApiResponse("Product out of stock."));
            default -> ResponseEntity.status(200).body(new ApiResponse("Product added successfully."));
        };
    }

    @PutMapping("/buy-product/{userId}/{productId}/{merchantId}")
    public ResponseEntity buyProduct(@PathVariable String userId, @PathVariable String productId, @PathVariable String merchantId) {
        char status = service.buyProduct(userId, productId, merchantId);
        return switch (status) {
            case '0' -> ResponseEntity.status(400).body(new ApiResponse("User not found."));
            case '1' -> ResponseEntity.status(400).body(new ApiResponse("Product not found."));
            case '2' -> ResponseEntity.status(400).body(new ApiResponse("Merchant not found."));
            case '3' -> ResponseEntity.status(400).body(new ApiResponse("MerchantStock not found."));
            case '4' -> ResponseEntity.status(400).body(new ApiResponse("Product out of stock."));
            case '5' -> ResponseEntity.status(400).body(new ApiResponse("Insufficient balance."));
            default -> ResponseEntity.status(200).body(new ApiResponse("Product bought successfully."));
        };
    }

    @PutMapping("/check-out/{userId}")
    public ResponseEntity checkOut(@PathVariable String userId) {
        char status = service.checkOut(userId);
        return switch (status) {
            case '0' -> ResponseEntity.status(400).body(new ApiResponse("User not found."));
            case '1' -> ResponseEntity.status(400).body(new ApiResponse("insufficient balance."));
            default -> ResponseEntity.status(200).body(new ApiResponse("Checked out successfully."));
        };
    }

    @GetMapping("/get-acquired-products/{userId}")
    public ResponseEntity getAllProductsAcquired(@PathVariable String userId) {
        ArrayList<String> list = service.getAllProductsAcquired(userId);
        if (list == null) return ResponseEntity.status(400).body(new ApiResponse("User not found."));
        return ResponseEntity.status(200).body(list);
    }

    @GetMapping("/get-Shopping-cart/{userId}")
    public ResponseEntity getShoppingCart(@PathVariable String userId) {
        ArrayList<String> cart = service.getShoppingCart(userId);
        if (cart == null) return ResponseEntity.status(400).body(new ApiResponse("User not found."));
        return ResponseEntity.status(200).body(cart);
    }

    @PutMapping("/add-to-balance/{userId}/{amount}")
    public ResponseEntity addToBalance(@PathVariable String userId, @PathVariable int amount) {
        char status = service.addToBalance(userId, amount);
        return switch (status) {
            case '0' -> ResponseEntity.status(400).body(new ApiResponse("Amount should be positive."));
            default -> ResponseEntity.status(200).body(new ApiResponse("Amount added successfully."));
        };

    }

    @PutMapping("/reduce-balance/{userId}/{amount}")
    public ResponseEntity reduceBalance(@PathVariable String userId, @PathVariable int amount) {
        char status = service.reduceBalance(userId, amount);
        return switch (status) {
            case '0' -> ResponseEntity.status(400).body(new ApiResponse("Amount should be positive."));
            case '1' -> ResponseEntity.status(400).body(new ApiResponse("Balance can not be reduced to below 0."));
            default -> ResponseEntity.status(200).body(new ApiResponse("Balance reduced successfully."));
        };

    }

    @DeleteMapping("/remove-from-cart/{userId}/{productId}")
    public ResponseEntity removeFromCart(@PathVariable String userId, @PathVariable String productId) {
        char status = service.removeFromCart(userId, productId);
        return switch (status) {
            case '0' -> ResponseEntity.status(400).body(new ApiResponse("User not found."));
            case '1' -> ResponseEntity.status(400).body(new ApiResponse("Product not found in the shopping cart."));
            default -> ResponseEntity.status(200).body(new ApiResponse("Product deleted successfully."));
        };
    }

    @PutMapping("/return-product/{userId}/{productId}")
    public ResponseEntity returnProduct(@PathVariable String userId, @PathVariable String productId) {
        char status = service.returnProduct(userId, productId);
        return switch (status) {
            case '0' -> ResponseEntity.status(400).body(new ApiResponse("User not found."));
            case '1' -> ResponseEntity.status(400).body(new ApiResponse("Product is not returnable."));
            case '2' -> ResponseEntity.status(400).body(new ApiResponse("Product not found."));
            default -> ResponseEntity.status(200).body(new ApiResponse("Product returned successfully."));
        };
    }

    @GetMapping("/get-cart-total/{userId}")
    public ResponseEntity getTotalMoneyAmountFromUserCart(@PathVariable String userId) {
        double totalMoney = service.getTotalMoneyAmountForUserCart(userId);
        if (totalMoney == -404) return ResponseEntity.status(404).body(new ApiResponse("User not found."));
        return ResponseEntity.status(200).body(totalMoney);
    }

}
