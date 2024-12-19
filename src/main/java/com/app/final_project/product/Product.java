package com.app.final_project.product;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;
    private int productPrice;
    private String productName;
    private int productQuantity;
    private List<String> productImagesUrl;
    private String description;
    private Boolean isDeleted = false;
}
