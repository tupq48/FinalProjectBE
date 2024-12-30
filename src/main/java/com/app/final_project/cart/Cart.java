package com.app.final_project.cart;

import java.time.LocalDateTime;
import java.util.List;

import com.app.final_project.enums.PaymentType;
import com.app.final_project.orderDetail.OrderDetail;
import com.app.final_project.product.Product;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

	@Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

 // ----------Related-----------
	@ManyToOne
	@JoinColumn(name = "productId", referencedColumnName = "productId", nullable = false)
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "userId", referencedColumnName = "user_id", nullable = false)
	private User user;
  
}
