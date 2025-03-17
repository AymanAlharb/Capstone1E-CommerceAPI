package com.example.capstone1ecommercewebsite.Controller;

import com.example.capstone1ecommercewebsite.Api.ApiResponse;
import com.example.capstone1ecommercewebsite.Model.MerchantStock;
import com.example.capstone1ecommercewebsite.Service.MerchantStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/merchant-stock")
@RequiredArgsConstructor
public class MerchantStockController {
    private final MerchantStockService service;

    @GetMapping("/get-all")
    public ResponseEntity getAll() {
        return ResponseEntity.status(200).body(service.getAll());
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid MerchantStock merchantStock, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        char status = service.createMerchantStock(merchantStock);
        return switch (status) {
            case '0' -> ResponseEntity.status(400).body(new ApiResponse("Merchant not found."));
            case '1' -> ResponseEntity.status(400).body(new ApiResponse("Id must be unique"));
            default -> ResponseEntity.status(200).body(new ApiResponse("MerchantStock added successfully."));
        };
    }

    @PutMapping("/update/{id}")
    public ResponseEntity add(@PathVariable String id, @RequestBody @Valid MerchantStock merchantStock, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        char status = service.updateMerchantStock(id, merchantStock);
        return switch (status) {
            case '0' -> ResponseEntity.status(400).body(new ApiResponse("Ids must match."));
            case '1' -> ResponseEntity.status(400).body(new ApiResponse("MerchantStock not found"));
            case '3' -> ResponseEntity.status(400).body(new ApiResponse("Merchant not found."));
            default -> ResponseEntity.status(200).body(new ApiResponse("MerchantStock updated successfully."));
        };
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        if (service.deleteMerchantStock(id))
            return ResponseEntity.status(200).body(new ApiResponse("MerchantStock deleted successfully."));
        return ResponseEntity.status(400).body(new ApiResponse("MerchantStock not found"));
    }

    @PutMapping("/restock/{productId}/{merchantId}/{amount}")
    public ResponseEntity restock(@PathVariable String productId, @PathVariable String merchantId, @PathVariable int amount) {
        char status = service.restock(productId, merchantId, amount);
        return switch (status) {
            case '0' -> ResponseEntity.status(400).body(new ApiResponse("Product not found."));
            case '1' -> ResponseEntity.status(400).body(new ApiResponse("Merchant not found."));
            case '2' -> ResponseEntity.status(400).body(new ApiResponse("MerchantStock not found."));
            default -> ResponseEntity.status(200).body(new ApiResponse("Restocked successfully."));
        };
    }
}
