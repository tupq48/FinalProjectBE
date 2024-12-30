package com.app.final_project.product.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequest {
//	private int productId;
    private int productPrice;
    private String productName;
    private int productQuantity;
    private List<String> productImagesUrl;
    private String description;
}
