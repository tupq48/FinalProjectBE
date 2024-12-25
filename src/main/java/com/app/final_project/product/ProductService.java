package com.app.final_project.product;
import com.app.final_project.eventImage.EventImageService;
import com.app.final_project.util.ImgBBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public List<Product> getAllProduct() {
        return productRepository.findAll().stream()
                .filter(pr -> !pr.getIsDeleted())
                .toList();
    }

    public Product saveProduct(Product product, List<MultipartFile> images) {
        var imageUrls = ImgBBUtils.uploadImages(images);
        product.setProductImagesUrl(imageUrls);
        return productRepository.save(product);
    }

    public Product getProductById(int id) {
        Product result = productRepository.findById(id).get();
        if (result.getIsDeleted()) {
            throw new RuntimeException("could not get product with id: " + id);
        }
        return result;
    }

    public Product deleteProduct(int id) {
        Product product = productRepository.findById(id).get();
        if (product == null || product.getIsDeleted()) {
            System.out.println("try to delete product with invalid Id: " + id);
            return null;
        }
        product.setIsDeleted(true);
        productRepository.save(product);
        return product;
    }

    public Product updateProduct(Product product, List<MultipartFile> images, boolean isAddMoreImages) {
        Product oldProduct = productRepository.findById(product.getProductId()).get();
        if (oldProduct == null || oldProduct.getIsDeleted()) {
            throw new RuntimeException("Could not update product with id: " + product.getProductId());
        }
        List<String> newList = new ArrayList<>();
        newList.addAll(oldProduct.getProductImagesUrl());
        if (images != null && isAddMoreImages) {
            var imageUrls = ImgBBUtils.uploadImages(images);
            newList.addAll(imageUrls);
        }
        Product result = Product.builder()
                                .description(product.getDescription())
                                .productId(product.getProductId())
                                .productName(product.getProductName())
                                .productQuantity(product.getProductQuantity())
                                .productPrice(product.getProductPrice())
                                .isDeleted(false)
                                .productImagesUrl(newList)
                                .build();
        return productRepository.save(result);
    }
}
