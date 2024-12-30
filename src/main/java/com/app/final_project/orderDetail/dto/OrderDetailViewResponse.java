package com.app.final_project.orderDetail.dto;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailViewResponse {
	private int id;
    private int price;
    private int quantity;
    private Boolean isDeleted = false;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
