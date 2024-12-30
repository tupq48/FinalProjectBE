package com.app.final_project.product;
import com.app.final_project.eventImage.EventImageService;
import com.app.final_project.util.ImgBBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    public Product saveProduct(Product product, List<MultipartFile> images) {
        var imageUrls = ImgBBUtils.uploadImages(images);
        product.setProductImagesUrl(imageUrls);
        product.setProductId(null);
        return productRepository.save(product);
    }

    public Product getProductById(int id) {
        return productRepository.findById(id).get();
    }
}
