package com.app.final_project.order;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.final_project.cart.Cart;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{
	@Query(nativeQuery = true, value = "SELECT * FROM orders WHERE orders.user_id = :userId")
    public List<Order> findAllByUserId(@Param("userId") int userId);
	
//	public Page<Order> findByUserId(int userId, Pageable pageable);
}
