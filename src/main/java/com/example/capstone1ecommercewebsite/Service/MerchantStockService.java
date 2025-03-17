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
public class MerchantStockService {
    private final MerchantService merchantService;
    private final ProductService productService;
    ArrayList<MerchantStock> merchantStocks = new ArrayList<>();

    public ArrayList<MerchantStock> getAll() {
        return merchantStocks;
    }

    public char createMerchantStock(MerchantStock merchantStock) {
        Merchant tempMerchant = merchantService.searchMerchant(merchantStock.getMerchantId());
        if (tempMerchant == null) return '0'; //Merchant not found.
        MerchantStock tempMerchantStock = searchMerchantStock(merchantStock.getId());
        if (tempMerchantStock != null) return '1'; //Merchant id already exits.
        merchantStocks.add(merchantStock);
        return '2'; //Created successfully.
    }

    public char updateMerchantStock(String id, MerchantStock merchantStock) {
        Merchant tempMerchant = merchantService.searchMerchant(merchantStock.getId());
        if (tempMerchant != null) return '3'; //Merchant not found.
        if (!id.equalsIgnoreCase(merchantStock.getId())) return '0'; //Ids does not match.
        MerchantStock tempMerchantStock = searchMerchantStock(id);
        if (tempMerchantStock == null) return '1'; //Merchant id already exits.
        merchantStocks.set(merchantStocks.indexOf(tempMerchantStock), merchantStock);
        return '2'; //Updated successfully.
    }

    public boolean deleteMerchantStock(String id) {
        MerchantStock tempMerchantStock = searchMerchantStock(id);
        if (tempMerchantStock == null) return false;
        merchantStocks.remove(tempMerchantStock);
        return true;
    }

    //Get MerchantStock object by the id.
    public MerchantStock searchMerchantStock(String id) {
        for (MerchantStock merchantStock : merchantStocks)
            if (merchantStock.getId().equalsIgnoreCase(id)) return merchantStock;
        return null;
    }

    //Get MerchantStock object by the product id.
    public MerchantStock getMerchantStockByStockProductId(String id) {
        for (MerchantStock merchantStock : merchantStocks)
            if (merchantStock.getProductId().equalsIgnoreCase(id)) return merchantStock;
        return null;
    }

    public char restock(String productId, String merchantId, int amount) {
        //Get the product.
        Product product = productService.searchProduct(productId);
        if (product == null) return '0'; //Product not found.
        //Get the merchant.
        Merchant merchant = merchantService.searchMerchant(merchantId);
        if(merchant == null) return '1'; //Merchant not found.
        //Get the merchantStock.
        MerchantStock merchantStock = getMerchantStockByStockProductId(productId);
        if (merchantStock == null) return '2'; //MerchantStock not found.
        //Restock
        merchantStock.setStock(merchantStock.getStock()+amount);
        return '3'; //Restocked successfully.
    }

    //This method get called by the user service checkOut method when user balance is insufficient.
    public void restockDueToUserInsufficientBalance(ArrayList<String> cart, User user) {
        for (String productId : cart) {
            //Get the merchantStock.
            MerchantStock tempMerchantStock = getMerchantStockByStockProductId(productId);
            //Restock
            tempMerchantStock.setStock(tempMerchantStock.getStock() + 1);
        }
    }

}


