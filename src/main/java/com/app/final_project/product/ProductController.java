package com.app.final_project.product;

import com.app.final_project.event.Event;
import com.app.final_project.event.dto.EventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.getAllProduct();
    }

    @PostMapping(consumes = {"multipart/form-data"}, value = ("/add"))
    public Product insertProduct(Product product,
                                 @RequestParam("images")List<MultipartFile> images) {
        return productService.saveProduct(product, images);
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable int id) {
        return productService.getProductById(id);
    }
}
