package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void addProduct(Product product) {
        productRepository.save(product);  // Save the entire product (name, price, category, image)
    }

    public List<Product> getProductsByCategory(String category) {
        System.out.println("Fetching products for category: " + category);  // Debug log
        return productRepository.findByCategory(category);
    }

    public void deleteProduct(int productId) {
        productRepository.deleteById(productId);
    }

    public Product getProductById(int productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID " + productId));
    }

    public Product updateProduct(int productId, Product productDetails) {
        Product product = productRepository.findById(productId).orElseThrow(() -> 
                new RuntimeException("Product not found with ID " + productId));

        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        product.setCategory(productDetails.getCategory());
        product.setImage(productDetails.getImage());  // Ensure this is updated with the new image filename

        return productRepository.save(product);  // Save the updated product
    }
}
