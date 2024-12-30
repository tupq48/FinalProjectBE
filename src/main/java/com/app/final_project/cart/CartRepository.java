package com.app.final_project.cart;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer>{
	@Query(nativeQuery = true, value =
			"SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END as exists_flag " +
			"FROM carts as c WHERE c.user_id = :userId AND c.product_id = :productId")
	public int existsByUserIdAndProductId(@Param("userId") int userId, @Param("productId") int productId);
	
	@Query(nativeQuery = true, value = "SELECT * FROM carts WHERE carts.user_id = :userId")
    List<Cart> findAllByUserId(@Param("userId") int userId);
	
//	public Page<Cart> findByUserId(int userId, Pageable pageable);
}
