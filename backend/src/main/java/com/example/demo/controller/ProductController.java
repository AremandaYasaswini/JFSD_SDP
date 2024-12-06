package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    @Autowired
    private ProductService productService;
    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image) {
        try {
            // Ensure the uploads directory exists
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();  // Create the directory if it doesn't exist
            }

            // Get the original filename
            String filename = image.getOriginalFilename();
            if (filename == null || filename.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid file: filename is empty");
            }

            // Log the file path before saving
            Path filePath = Paths.get(UPLOAD_DIR + filename);
            System.out.println("Uploading file to: " + filePath.toAbsolutePath());

            // Save the file to the uploads directory
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Return the filename to the frontend
            return ResponseEntity.ok(filename);
        } catch (IOException e) {
            e.printStackTrace();  // Print the stack trace for debugging
            return ResponseEntity.status(500).body("Failed to upload image: " + e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        // Log the product details to check if the data is being received correctly
        System.out.println("Received product: " + product);

        productService.addProduct(product); // Save the complete product to the database
        return ResponseEntity.ok("Product added successfully");
    }

    @GetMapping("/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductsByCategory(category);

        // Only prepend the full image URL for "New Launch" category
        if ("New Launch".equalsIgnoreCase(category)) {
            String baseUrl = "http://localhost:8080/api/products/image/";
            products.forEach(product -> {
                product.setImage(baseUrl + product.getImage());  // Assuming you have a `setImageUrl` method
            });
        }

        return products;
    }

    @GetMapping("/image/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) throws IOException {
        Path path = Paths.get(UPLOAD_DIR + filename);
        
        // Log the file path and check if the file exists
        System.out.println("Accessing file: " + path.toAbsolutePath());
        
        byte[] imageBytes = Files.readAllBytes(path);
        
        return ResponseEntity.ok()
                .header("Content-Type", "image/jpg") // Adjust based on the file type (e.g., image/png)
                .body(imageBytes);
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable int productId, @RequestBody Product productDetails) {
        try {
            // Ensure the product exists first
            Product existingProduct = productService.getProductById(productId);
            if (existingProduct == null) {
                return ResponseEntity.status(404).body(null);  // Product not found
            }

            // Update the product fields
            existingProduct.setName(productDetails.getName());
            existingProduct.setPrice(productDetails.getPrice());

            // Ensure the image is not overwritten or null
            if (productDetails.getImage() != null && !productDetails.getImage().isEmpty()) {
                existingProduct.setImage(productDetails.getImage());
            }

            // Save the updated product to the database using addProduct (which calls save internally)
            productService.addProduct(existingProduct);

            return ResponseEntity.ok(existingProduct);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);  // Server error
        }
    }

    @GetMapping("/id/{productId}")  // Update the path to make it unique
    public ResponseEntity<Product> getProductById(@PathVariable int productId) {
        try {
            Product product = productService.getProductById(productId);
            if (product == null) {
                return ResponseEntity.status(404).body(null);  // Product not found
            }
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);  // Server error
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
