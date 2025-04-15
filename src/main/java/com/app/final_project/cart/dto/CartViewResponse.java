package com.app.final_project.cart.dto;

import java.time.LocalDateTime;

import com.app.final_project.product.Product;
import com.app.final_project.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartViewResponse {
	private int id;
    private int quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
	private Product product;
}
