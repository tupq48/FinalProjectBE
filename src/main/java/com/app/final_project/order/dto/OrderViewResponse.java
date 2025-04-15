package com.app.final_project.order.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.app.final_project.enums.OrderStatus;
import com.app.final_project.enums.PaymentType;
import com.app.final_project.orderDetail.OrderDetail;
import com.app.final_project.orderDetail.dto.OrderDetailViewResponse;
import com.app.final_project.product.Product;
import com.app.final_project.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderViewResponse {
	private int id;
    private String description;
    private PaymentType paymentType;
    private String address;
    private String phoneNumber;
    private OrderStatus status;
    private int totalPrice;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
//    private User user;
    private List<OrderDetailViewResponse> orderDetails;
    private String userId;
}
