package com.cloud.shop;

import com.cloud.shop.entities.Category;
import com.cloud.shop.entities.Inventory;
import com.cloud.shop.entities.Product;
import com.cloud.shop.repositories.CategoryRepository;
import com.cloud.shop.repositories.InventoryRepository;
import com.cloud.shop.repositories.ProductRepository;
import com.cloud.shop.s3.S3ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;


@Component
public class DataInitializer {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private S3ImageService s3ImageService;

//   @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting Data Initialization...");

        // Create Categories
        Map<String, Category> categoryMap = new HashMap<>();
        categoryMap.put("A", new Category("A"));
        categoryMap.put("B", new Category("B"));
        categoryMap.put("C", new Category("C"));

        // Save categories if they don’t already exist
        for (Category category : categoryMap.values()) {
            categoryRepository.save(category);
        }

        // Product Data
        Object[][] products = {
                {1, "Hertford Upholstered Chair", 101, "hertford-upholstered-chair", "A"},
                {2, "Abingdon Swivel Upholstered Chair", 155, "abingdon-swivel-upholstered-chair", "C"},
                {3, "Jesse Minimore Modern Style Chair", 181, "jesse-minimore-modern-style-chair", "A"},
                {4, "Jesse Minimore Contemporary Chair", 201, "jesse-minimore-contemporary-chair", "B"},
                {5, "Bolanle Tufted Upholstered Armchair", 251, "bolanle-tufted-upholstered-armchair", "C"},
                {6, "Jacques Upholstered Armchair", 111, "jacques-upholstered-armchair", "B"},
                {7, "Leston Wide Upholstered Fabric Chair", 121, "leston-wide-upholstered-fabric-chair", "A"},
                {8, "Stephanie 27.5\" Wide Tufted Armchair", 220, "stephanie-275-wide-tufted-armchair", "C"}
        };


        for (Object[] prodData : products) {
            int id = (int) prodData[0];
            String name = (String) prodData[1];
            int price = (int) prodData[2];
            String slug = (String) prodData[3];
            String categoryCode = (String) prodData[4];

            // Fetch Category
            Category category = categoryMap.get(categoryCode);
            if (category == null) {
                System.out.println("Category not found: " + categoryCode);
                continue;
            }

            // Create and save Product
            Product product = new Product();
            product.setName(name);
            product.setPrice((double) price);
            product.setDescription("Experience superior comfort and support with our Ergonomic Upholstered Office Chair—designed for both productivity and relaxation.");
            product.setSlug(slug);
            product.setCategory(category);
            productRepository.save(product);

            // Inventory
            Inventory inventory = new Inventory();
            inventory.setProduct(product);
            inventory.setQuantity(20 * id);
            inventory.setRestockThreshold(2);
            inventoryRepository.save(inventory);

            // Upload Image to S3
            String imagePath = "static/images/" + id + ".png";
            ClassPathResource imgFile = new ClassPathResource(imagePath);

            if (imgFile.exists()) {
                File file = imgFile.getFile();
                MultipartFile multipartFile = convertFileToMultipartFile(file);
                String imageUrl = s3ImageService.uploadImage(multipartFile);
                product.setImageUrl(imageUrl);
                productRepository.save(product);
                System.out.println("Uploaded image: " + imageUrl);
            } else {
                System.out.println("Image not found: " + imagePath);
            }
        }
        System.out.println("Data Initialization Completed!");
    }

    private MultipartFile convertFileToMultipartFile(File file) throws IOException {
        return new MockMultipartFile(
                file.getName(),
                file.getName(),
                "image/png",
                Files.readAllBytes(file.toPath())
        );
    }
}
