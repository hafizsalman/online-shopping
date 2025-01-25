package com.cloud.shop;
import com.cloud.shop.entities.Category;
import com.cloud.shop.entities.Inventory;
import com.cloud.shop.entities.Product;
import com.cloud.shop.entities.ProductImage;
import com.cloud.shop.repositories.CategoryRepository;
import com.cloud.shop.repositories.InventoryRepository;
import com.cloud.shop.repositories.ProductImageRepository;
import com.cloud.shop.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.file.Files;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        // Add sample products with complete information and images

        Category electronics = new Category("Electronics");
        categoryRepository.save(electronics);

        Category books = new Category("Books");
        categoryRepository.save(books);

        for (int i = 1; i <= 10; i++) {
            Product product = new Product();
            product.setName("Product " + i);
            product.setDescription("Description for Product " + i);
            product.setPrice(100.00 * i);
            product.setStockQuantity(10 * i);

            if (i % 2 == 0) {
                product.setCategory(electronics);
            } else {
                product.setCategory(books);
            }
            productRepository.save(product);

            // Add inventory for the product
            Inventory inventory = new Inventory();
            inventory.setProduct(product);
            inventory.setQuantity(20 * i);
            inventory.setRestockThreshold(2);
            inventoryRepository.save(inventory);

            // Load image from resources folder
            String imagePath = "static/images/product" + i + ".jpg";
            ClassPathResource imgFile = new ClassPathResource(imagePath);
            byte[] imageData = Files.readAllBytes(imgFile.getFile().toPath());

            // Save the image
            ProductImage productImage = new ProductImage();
            productImage.setProduct(product);
            System.out.println("Image data type: " + imageData.getClass().getName());

            productImage.setImageData(imageData);
            productImage.setPrimary(true);
            productImageRepository.save(productImage);
        }
    }
}
