package com.app.final_project.product;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.final_project.userInfor.UserInfor;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
}
