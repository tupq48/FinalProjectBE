package com.app.final_project.product;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.app.final_project.product.document.ProductES;
import com.app.final_project.product.dto.CreateProductRequest;
import com.app.final_project.util.ImgBBUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.processing.Completion;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;
    private final ElasticsearchClient elasticsearchClient;
    private final ProductSearchRepository esRepo;

    public ProductService(ElasticsearchClient elasticsearchClient, ProductSearchRepository esRepo) {
        this.elasticsearchClient = elasticsearchClient;
        this.esRepo = esRepo;
    }

    public List<Product> getAllProduct() {
        return productRepository.findAll().stream()
                .filter(pr -> !pr.getIsDeleted())
                .toList();
    }

    public Product saveProduct(CreateProductRequest product, List<MultipartFile> images) {
        var imageUrls = ImgBBUtils.uploadImages(images);
        Product productCreated = new Product();
        productCreated.setProductPrice(product.getProductPrice());
        productCreated.setProductName(product.getProductName());
        productCreated.setProductQuantity(product.getProductQuantity());
        productCreated.setDescription(product.getDescription());
        productCreated.setProductImagesUrl(imageUrls);
        Product produced = productRepository.save(productCreated);

        ProductES productES = ProductES.builder()
                .description(produced.getDescription())
                .productId(produced.getProductId())
                .productName(produced.getProductName())
                .productQuantity(produced.getProductQuantity())
                .productPrice(produced.getProductPrice())
                .isDeleted(produced.getIsDeleted())
                .productImagesUrl(imageUrls)
                .build();
        esRepo.save(productES);
        return produced;
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
                                .isDeleted(product.getIsDeleted())
                                .productImagesUrl(newList)
                                .build();
        Product produced = productRepository.save(result);

        ProductES productES = ProductES.builder()
                .description(produced.getDescription())
                .productId(produced.getProductId())
                .productName(produced.getProductName())
                .productQuantity(produced.getProductQuantity())
                .productPrice(produced.getProductPrice())
                .isDeleted(produced.getIsDeleted())
                .productImagesUrl(newList)
                .build();
        esRepo.save(productES);
        return produced;
    }
    public List<ProductES> search(String keyword) {
        return esRepo.findByProductNameContaining(keyword);
    }
    public List<ProductES> searchProductByName(String query) {
        try {
            SearchResponse<ProductES> response = elasticsearchClient.search(s -> s
                            .index("products")
                            .query(q -> q
                                    .match(m -> m
                                            .field("productName")
                                            .query(query)
                                            .fuzziness("AUTO") // Cho phép sai chính tả
                                    )
                            ),
                    ProductES.class
            );

            return response.hits().hits().stream()
                    .map(Hit::source)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
            return List.of(); // Trả về danh sách rỗng nếu có lỗi
        }
    }

}
