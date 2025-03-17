# E-commerce API Documentation
This project is part of the **Web Development Program Using Java and SpringBoot** presented by **Tuwaiq Academy**.
## Models

### 1. User
**Main Attributes:**
1. `id`
2. `user_name`
3. `password`
4. `email`
5. `role`
6. `balance`

**Added Attributes:**
1. `shopping_cart`  
   - `ArrayList<String>` to store the `productIDs` of the products in the user cart.
2. `acquired_products_list`  
   - `ArrayList<String>` to store the `productIDs` of the products the user bought.
3. `hasDiscount`  
   - `boolean` to check if the user has a discount.
4. `discountAmount`

---

### 2. Product
**Main Attributes:**
1. `id`
2. `name`
3. `price`
4. `categoryId`

**Added Attributes:**
1. `isReturnable`  
   - To check if the product can be returned.

---

### 3. Merchant
**Main Attributes:**
1. `id`
2. `name`

**Added Attributes:**
1. `sold_products`  
   - `ArrayList<String>` to store the `id` of products sold.
2. `merchant_total_money`

---

### 4. MerchantStock
**Main Attributes:**
1. `id`
2. `productId`
3. `merchantId`
4. `stock`

---

### 5. Category
**Main Attributes:**
1. `id`
2. `name`

---

## Controllers and Endpoints

### 1. User Controller
1. **CRUD**
2. **add-to-cart**  
   - Takes `(userId, productId, merchantId)` as `@PathVariables`.  
   - Calls the `addToCart` method in the `UserService` and handles the status of the method in a switch statement.
3. **buy-product**  
   - Takes `(userId, productId, merchantId)` as `@PathVariables`.  
   - Calls the `buyProduct` method in the `UserService` and handles the status of the method in a switch statement.
4. **check-out**  
   - Takes `(userId)` as `@PathVariable`.  
   - Calls the `checkOut` method in the `UserService` and handles the status of the method in a switch statement.
5. **get-acquired-products**  
   - Takes `(userId)` as `@PathVariable`.  
   - Calls the `getAllProductsAcquired` method in the `UserService` and returns the user products list.
6. **get-shopping-cart**  
   - Takes `(userId)` as `@PathVariable`.  
   - Calls the `getShoppingCart` method in the `UserService` and returns the user cart list.
7. **add-to-balance**  
   - Takes `(userId, amount)` as `@PathVariables`.  
   - Calls the `addToBalance` method in the `UserService` and handles the status of the method in a switch statement.
8. **reduce-balance**  
   - Takes `(userId, amount)` as `@PathVariables`.  
   - Calls the `reduceBalance` method in the `UserService` and handles the status of the method in a switch statement.
9. **remove-from-cart**  
   - Takes `(userId, productId)` as `@PathVariables`.  
   - Calls the `removeFromCart` method in the `UserService` and handles the status of the method in a switch statement.
10. **return-product**  
    - Takes `(userId, productId)` as `@PathVariables`.  
    - Calls the `returnProduct` method in the `UserService` and handles the status of the method in a switch statement.
11. **get-cart-total**  
    - Takes `(userId)` as `@PathVariable`.  
    - Calls the `getTotalMoneyAmountForUserCart` method in the `UserService` and returns the money.

---

### 2. Product Controller
1. **CRUD**

---

### 3. Merchant Controller
1. **CRUD**
2. **get-sold-list**  
   - Takes `(merchantId)` as `@PathVariable`.  
   - Calls the `getSoldProductsList` method in the `MerchantService` and returns the merchant sold products list.

---

### 4. MerchantStock Controller
1. **CRUD**
2. **Restock**  
   - Takes `(productId, merchantId, amount)` as `@PathVariables`.  
   - Calls the `restock` method in the `MerchantStockService` and handles the status of the method in a switch statement.

---

### 5. Category Controller
1. **CRUD**

---

## Services

### UserService
1. **addToCart**  
   - This method adds products to the user `shopping_cart`, which they can later buy (checkOut).  
   - First, it validates the IDs and stock using the `validateIdsAndStock` method. If the method passes, it gets the user, the `MerchantStock`, adds the product to the user cart, reduces the stock, and returns `'5'`, which tells the controller that the product was added successfully.
2. **buyProduct**  
   - This method allows the user to buy the product directly without adding it to the cart.  
   - First, it validates the IDs and stock using the `validateIdsAndStock` method. If the method passes, it gets the objects, applies a discount if applicable using the `applyDiscount` method, checks the user balance, and returns `'5'` if the user does not have enough money. Otherwise, it reduces the stock, reduces the user balance, adds the product to the user's products list, adds the product to the merchant's sold list, increases the merchant's total money, and returns `'6'` to indicate success.
3. **validateIdsAndStock**  
   - This is a helper method for the `buyProduct` and `addToCart` methods. It validates the IDs of the user, product, and merchant and checks if the product is in stock.
4. **checkOut**  
   - This method processes the user's `shopping_cart` and checks the user out.  
   - First, it checks if the user exists and returns `'0'` if not. Then, it calculates the total price using the `sumUpTotalPrice` method, applies a discount using the `applyDiscount` method, checks the user balance, and returns `'1'` if the user does not have enough money. If all conditions pass, it loops through the user's cart, gets the objects, and adds them in the same process as the `buy` method. Finally, it returns `'2'` to indicate success.
5. **sumUpTotalPrice**  
   - This is a helper method for the `checkOut` method. It calculates the total price of the cart by looping through the cart and adding the total to the `totalPrice` variable.
6. **getAllProductsAcquired**  
   - This method returns the user's `acquired_products_list`.  
   - It first checks if the user exists and returns `null` if not, which tells the controller that the user does not exist. Otherwise, it returns the list.
7. **getShoppingCart**  
   - This method returns the user's `shopping_cart`.  
   - It first checks if the user exists and returns `null` if not, which tells the controller that the user does not exist. Otherwise, it returns the list.
8. **searchUser**  
   - This is a helper method that loops through the list of users to find a user and returns `null` if the user is not found.
9. **addToBalance**  
   - This method increases the user's balance.
10. **reduceBalance**  
    - This method decreases the user's balance.
11. **removeFromCart**  
    - This method removes a product from the user's cart using the user ID and product ID.  
    - First, it checks if the user exists and returns `'0'` if not. Otherwise, it loops through the user's cart to find the product. If found, it removes the product, restocks it, and returns `'2'` to indicate success. If the product is not found, it returns `'1'`.
12. **returnProduct**  
    - This method works similarly to `removeFromCart`, but it also checks if the product is returnable. It removes the product from the merchant's list, restocks it, and adjusts the user's balance and the merchant's total money.
13. **getTotalMoneyAmountForUserCart**  
    - This method returns the total money of the cart.  
    - First, it checks if the user exists and returns `-404` if not. Otherwise, it calculates the total price using the `sumUpTotalPrice` method, applies a discount using the `applyDiscount` method, and returns the total price of the cart.
14. **applyDiscount**  
    - This is a helper method to check if the user has a discount and return the new price. If not, it returns the same price.

---

### ProductService
- This service does not have any extra methods (only CRUD). The only difference is in the `add` method, which checks if the category exists since the product depends on the category.

---

### CategoryService
- Same as the `ProductService`, except that the product does not depend on any other model.

---

### MerchantStockService
1. **getMerchantStockByStockProductId**  
   - This is a helper method to get the `MerchantStock` by the product ID.
2. **restock**  
   - This method adds to the `stock` attribute after checking if all objects exist.
3. **restockDueToUserInsufficientBalance**  
   - This is a helper method for the `UserService.checkOut` method. If the user does not have enough money, this method is called to restock the products in the user's cart by looping through the cart.

---

### MerchantService
1. **getSoldProductsList**  
   - Returns the `sold_products` list.
   - ## Contact Information

For any questions, feedback, or collaboration opportunities, feel free to reach out:

- **Email**: Ayman.f.alharbi@gmail.com
