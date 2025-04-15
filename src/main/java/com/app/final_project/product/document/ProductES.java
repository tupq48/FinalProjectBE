package com.app.final_project.product.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.annotation.processing.Completion;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "products")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ProductES {
    @Id
    private Integer productId;
    private int productPrice;
    private String productName;
    private int productQuantity;
    private List<String> productImagesUrl;
    private String description;
    private Boolean isDeleted = false;
}
