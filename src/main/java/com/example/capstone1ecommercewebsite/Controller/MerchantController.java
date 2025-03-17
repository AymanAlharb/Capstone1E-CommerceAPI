package com.example.capstone1ecommercewebsite.Controller;

import com.example.capstone1ecommercewebsite.Api.ApiResponse;
import com.example.capstone1ecommercewebsite.Model.Merchant;
import com.example.capstone1ecommercewebsite.Service.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/merchant")
@RequiredArgsConstructor
public class MerchantController {
    private final MerchantService service;

    @GetMapping("/get-all")
    public ResponseEntity getAll() {
        return ResponseEntity.status(200).body(service.getAll());
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid Merchant merchant, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        if (service.createMerchant(merchant))
            return ResponseEntity.status(200).body(new ApiResponse("Merchant added successfully."));
        return ResponseEntity.status(400).body(new ApiResponse("Id must be unique"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity add(@PathVariable String id, @RequestBody @Valid Merchant merchant, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        char status = service.updateMerchant(id, merchant);
        return switch (status) {
            case '0' -> ResponseEntity.status(400).body(new ApiResponse("Ids must match."));
            case '1' -> ResponseEntity.status(400).body(new ApiResponse("Merchant not found"));
            default -> ResponseEntity.status(200).body(new ApiResponse("Merchant updated successfully."));
        };
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        if (service.deleteMerchant(id))
            return ResponseEntity.status(200).body(new ApiResponse("Merchant deleted successfully."));
        return ResponseEntity.status(400).body(new ApiResponse("Merchant not found"));
    }

    @GetMapping("/get-sold-list/{merchantId}")
    public ResponseEntity getSoldProductsList(@PathVariable String merchantId) {
        ArrayList<String> list = service.getSoldProductsList(merchantId);
        if (list == null) return ResponseEntity.status(404).body(new ApiResponse("Merchant not found."));
        return ResponseEntity.status(200).body(list);
    }
}
