package com.example.capstone1ecommercewebsite.Service;

import com.example.capstone1ecommercewebsite.Model.Merchant;
import com.example.capstone1ecommercewebsite.Model.MerchantStock;
import com.example.capstone1ecommercewebsite.Model.Product;
import com.example.capstone1ecommercewebsite.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MerchantService {
    private final ProductService productService;
    //private final MerchantStockService merchantStockService;
    ArrayList<Merchant> merchants = new ArrayList<>();

    public ArrayList<Merchant> getAll() {
        return merchants;
    }

    public boolean createMerchant(Merchant merchant) {
        Merchant tempMerchant = searchMerchant(merchant.getId());
        if (tempMerchant != null) return false; //Merchant id already exists.
        merchants.add(merchant);
        return true; //Created successfully.
    }

    public char updateMerchant(String id, Merchant merchant) {
        if (!id.equalsIgnoreCase(merchant.getId())) return '0'; //Ids does not match.
        Merchant tempMerchant = searchMerchant(id);
        if (tempMerchant == null) return '1'; //Merchant not found.
        merchants.set(merchants.indexOf(tempMerchant), merchant);
        return '2'; //Updated successfully.
    }

    public boolean deleteMerchant(String id) {
        Merchant tempMerchant = searchMerchant(id);
        if (tempMerchant == null) return false; //Merchant not found.
        merchants.remove(tempMerchant);
        return true;
    }

    public ArrayList<String> getSoldProductsList(String merchantId) {
        Merchant merchant = searchMerchant(merchantId);
        if (merchant == null) return null; //Merchant not found.
        return merchant.getSold_products();
    }

    //Get Merchant object by merchant id.
    public Merchant searchMerchant(String id) {
        for (Merchant merchant : merchants) if (merchant.getId().equalsIgnoreCase(id)) return merchant;
        return null;
    }
}


