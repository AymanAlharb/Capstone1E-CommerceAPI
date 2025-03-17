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
public class UserService {
    ArrayList<User> users = new ArrayList<>();
    private final ProductService productService;
    private final MerchantStockService merchantStockService;
    private final MerchantService merchantService;

    public ArrayList<User> getAll() {
        return users;
    }

    public boolean createUser(User user) {
        User tempUser = searchUser(user.getId());
        if (tempUser != null) return false;
        users.add(user);
        return true;
    }

    public char updateUser(String id, User user) {
        if (!id.equalsIgnoreCase(user.getId())) return '0';  //Ids does not match.
        User tempUser = searchUser(id);
        if (tempUser == null) return '1'; //User not found.
        users.set(users.indexOf(tempUser), user);
        return '2'; //Updated successfully.
    }

    public boolean deleteUser(String id) {
        User tempUser = searchUser(id);
        if (tempUser == null) return false;
        users.remove(tempUser);
        return true;
    }

    //Add products to the user shopping cart
    public char addToCart(String userId, String productId, String merchantId) {
        //Validate ids and check stock.
        char status = validateIdsAndStock(userId, productId, merchantId);
        if (status != '5') return status; //Problem with the ids or stock.
        //Get the objects.
        User tempUser = searchUser(userId);
        MerchantStock tempMerchantstock = merchantStockService.getMerchantStockByStockProductId(productId);
        //Add product to user cart.
        tempUser.getShopping_cart().add(productId);
        //Reduce product stock.
        tempMerchantstock.setStock(tempMerchantstock.getStock() - 1);
        //Product added successfully.
        return '5';
    }

    public char buyProduct(String userId, String productId, String merchantId) {
        //Validate ids and check stock.
        char status = validateIdsAndStock(userId, productId, merchantId);
        if (status != '5') return status; //Problem with the ids or stock.
        //Get the objects.
        Product product = productService.searchProduct(productId);
        User user = searchUser(userId);
        //Apply discount.
        double productPrice = applyDiscount(user.isHasDiscount(), product.getPrice(), user.getDiscountAmount());
        if (user.getBalance() < productPrice) return '5'; //Insufficient balance.
        //Reduce stock.
        MerchantStock merchantStock = merchantStockService.getMerchantStockByStockProductId(productId);
        merchantStock.setStock(merchantStock.getStock() - 1);
        //Reduce balance.
        user.setBalance(user.getBalance() - productPrice);
        //Add product to the acquired list.
        user.getAcquired_products_list().add(productId);
        //Add to the sold list
        Merchant merchant = merchantService.searchMerchant(merchantStockService.getMerchantStockByStockProductId(productId).getMerchantId());
        merchant.getSold_products().add(productId);
        //Add the price to the merchant total money.
        merchant.setMerchant_total_money(merchant.getMerchant_total_money() + productPrice);
        return '6'; //Product bought successfully.
    }

    //Method to check the availability of the User, Product, Merchant and MerchantStock objects than check the stock.
    public char validateIdsAndStock(String userId, String productId, String merchantId) {
        //Get the user and check if the user available in the system.
        User tempUser = searchUser(userId);
        if (tempUser == null) return '0'; //User not found
        //get the product and check if the product available in the system.
        Product tempProduct = productService.searchProduct(productId);
        if (tempProduct == null) return '1'; //Product not found
        //Get the Merchant and check if it's found in the system.
        Merchant merchant = merchantService.searchMerchant(merchantId);
        if (merchant == null) return '2'; //Merchant not found.
        //Get merchantStock and check product stock.
        MerchantStock tempMerchantstock = merchantStockService.getMerchantStockByStockProductId(productId);
        if (tempMerchantstock == null) return '3'; //MerchantStock not found.
        if (tempMerchantstock.getStock() < 1) return '4'; //Product out of stock
        return '5'; //All objects available.
    }

    //checkout
    public char checkOut(String userId) {
        User user = searchUser(userId);
        if (user == null) return '0'; //User not found.
        //Calculate the total price.
        double totalPrice = sumUpTotalPrice(user, user.getShopping_cart());
        //Apply discount .
        totalPrice = applyDiscount(user.isHasDiscount(), totalPrice, user.getDiscountAmount());
        //Check user balance.
        if (totalPrice > user.getBalance()) {
            //Insufficient balance: restock products and clear cart.
            merchantStockService.restockDueToUserInsufficientBalance(user.getShopping_cart(), user);
            user.getShopping_cart().clear();
            return '1'; //Insufficient balance.
        }
        //Loop through the user cart.
        for (String productId : user.getShopping_cart()) {
            //Get the product.
            Product product = productService.searchProduct(productId);
            if (product == null) continue; // Skip if product not found
            //Get the MerchantStock.
            MerchantStock merchantStock = merchantStockService.getMerchantStockByStockProductId(productId);
            if (merchantStock == null) continue; // Skip if MerchantStock not found
            //Get the Merchant.
            Merchant merchant = merchantService.searchMerchant(merchantStock.getMerchantId());
            if (merchant == null) continue; // Skip if merchant not found
            //Get the product price.
            double productPrice = product.getPrice();
            //Apply discount.
            productPrice = applyDiscount(user.isHasDiscount(), productPrice, user.getDiscountAmount());
            //Reduce stock.
            merchantStock.setStock(merchantStock.getStock() - 1);
            //Add the product to the merchant list.
            merchant.getSold_products().add(productId);
            //Add the price to the merchant total money.
            merchant.setMerchant_total_money(merchant.getMerchant_total_money() + productPrice);
            //Add the product to the user products list.
            user.getAcquired_products_list().add(productId);
        }
        //Clear the shopping cart.
        user.getShopping_cart().clear();
        //Reduce user balance.
        user.setBalance(user.getBalance() - totalPrice);
        return '2'; // Checked out successfully.
    }

    //Add products to the acquired list.
    public void addProductsToAcquiredList(User user, ArrayList<String> cart) {
        for (String productId : cart) {
            //Add to the acquired list
            user.getAcquired_products_list().add(productId);
        }
    }

    //Sum up the total price of the cart
    public double sumUpTotalPrice(User user, ArrayList<String> cart) {
        double totalPrice = 0;
        for (String productId : cart) {
            //Get the product.
            Product tempProduct = productService.searchProduct(productId);
            //Added the price to the totalPrice
            totalPrice += tempProduct.getPrice();
        }
        return totalPrice;
    }

    //Get all the products bought by the user
    public ArrayList<String> getAllProductsAcquired(String userId) {
        User user = searchUser(userId);
        if (user == null) return null;
        return user.getAcquired_products_list();
    }

    //Get user cart
    public ArrayList<String> getShoppingCart(String userId) {
        User user = searchUser(userId);
        if (user == null) return null; //User not found
        return user.getShopping_cart();
    }

    //Search for the user by the user id.
    public User searchUser(String id) {
        for (User user : users) if (user.getId().equalsIgnoreCase(id)) return user;
        return null;
    }

    //Add money to user balance.
    public char addToBalance(String userId, int amount) {
        if (amount < 0) return '0'; //Amount should be positive.
        User user = searchUser(userId);
        user.setBalance(user.getBalance() + amount);
        return '1'; //Amount added successfully.
    }

    //Reduce user balance.
    public char reduceBalance(String userId, int amount) {
        if (amount < 0) return '0'; //Amount should be positive.
        User user = searchUser(userId);
        if (user.getBalance() - amount < 0) return '1'; //Balance can not be reduced to below 0.
        user.setBalance(user.getBalance() - amount);
        return '2'; //Balance reduced successfully.
    }

    //Remove product from user shopping cart.
    public char removeFromCart(String userId, String productId) {
        User user = searchUser(userId);
        if (user == null) return '0'; //User not found.
        for (String product : user.getShopping_cart())
            if (product.equalsIgnoreCase(productId)) {
                //Remove product.
                user.getShopping_cart().remove(product);
                //Restock
                merchantStockService.getMerchantStockByStockProductId(productId).setStock(merchantStockService.getMerchantStockByStockProductId(productId).getStock() + 1);
                return '2'; //Deleted successfully.
            }

        return '1'; //Product not found.

    }

    //Return product from user acquired list.
    public char returnProduct(String userId, String productId) {
        User user = searchUser(userId);
        if (user == null) return '0'; //User not found.

        //Check if the product is in the list.
        if (!user.getAcquired_products_list().contains(productId)) {
            return '2'; // Product not found.
        }

        //Get the product object.
        Product product = productService.searchProduct(productId);
        if (product == null) return '2'; //Product not found.

        //Check if the product is returnable.
        if (!product.isReturnable()) return '1'; //Product is not returnable.

        //Remove the product from the list.
        user.getAcquired_products_list().remove(productId);

        //Add the money back to the user balance.
        user.setBalance(user.getBalance() + product.getPrice());

        //Get the MerchantStock for the product.
        MerchantStock merchantStock = merchantStockService.getMerchantStockByStockProductId(productId);
        if (merchantStock != null) {
            //Get the Merchant.
            Merchant merchant = merchantService.searchMerchant(merchantStock.getMerchantId());
            if (merchant != null) {
                //Remove the product from the list.
                merchant.getSold_products().remove(productId);
                //Reduce the money from the merchant total money.
                merchant.setMerchant_total_money(merchant.getMerchant_total_money() - product.getPrice());
            }
        }

        return '3'; //Product returned successfully.
    }

    //Get the total money amount for the user cart
    public double getTotalMoneyAmountForUserCart(String userId) {
        //Get the user.
        User user = searchUser(userId);
        if (user == null) return -404.0; //User not found.
        //Get the total price.
        double totalPrice = sumUpTotalPrice(user, user.getShopping_cart());
        //Apply discount.
        totalPrice = applyDiscount(user.isHasDiscount(), totalPrice, user.getDiscountAmount());
        //Return the total.
        return totalPrice;
    }

    public double applyDiscount(boolean hasDiscount, double price, double discount) {
        if (hasDiscount) return (price - (price * discount));
        return price;
    }

}


