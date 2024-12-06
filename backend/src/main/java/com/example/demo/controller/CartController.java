package com.example.demo.controller;

import com.example.demo.model.CartItem;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:3000")
public class CartController {

    @Autowired
    private CartService cartService;

    // Add a product to the cart
    @PostMapping("/add")
    public ResponseEntity<CartItem> addToCart(@RequestParam Long userId, @RequestParam int productId, @RequestParam int quantity) {
        CartItem cartItem = cartService.addToCart(userId, productId, quantity);
        return ResponseEntity.ok(cartItem);
    }

    // Get all items in the user's cart
    @GetMapping("/details")
    public ResponseEntity<List<CartItem>> getCartDetails(@RequestParam Long userId) {
        List<CartItem> cartItems = cartService.getCartDetails(userId);
        return ResponseEntity.ok(cartItems);
    }

    // Remove a product from the cart
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestParam Long userId, @RequestParam int productId) {
        cartService.removeFromCart(userId, productId);
        return ResponseEntity.ok("Item removed from cart");
    }

    // Update the quantity of an item in the cart
    @PostMapping("/updateQuantity")
    public ResponseEntity<CartItem> updateQuantity(@RequestParam Long userId, @RequestParam int productId, @RequestParam int quantity) {
        CartItem updatedCartItem = cartService.updateQuantity(userId, productId, quantity);
        return ResponseEntity.ok(updatedCartItem);
    }
}
