package com.app.final_project.order;
import java.time.LocalDateTime;
import java.util.List;

import com.app.final_project.enums.OrderStatus;
import com.app.final_project.enums.PaymentType;
import com.app.final_project.orderDetail.OrderDetail;
import com.app.final_project.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public class Order {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String description;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType = PaymentType.VNPAY;
    
    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PROCESS;

    @Column(nullable = false)
    private int totalPrice;
    
    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

 // ----------Related-----------
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;
}
