package com.app.final_project.cart;

import java.util.List;

import com.app.final_project.cart.dto.CartViewResponse;
import com.app.final_project.cart.dto.CreateCartRequest;
import com.app.final_project.cart.dto.UpdateCartRequest;
import com.app.final_project.common.PaginationRequest;
import com.app.final_project.common.PaginationResponse;

public interface CartService {
	public List<CartViewResponse> getAllCartsByUser(int userId);
	public CartViewResponse getCartById(int id);
//	public PaginationResponse<Cart> getAllCartsPaginationByUser(int userId, PaginationRequest paginationRequest);
	public CartViewResponse createCart(CreateCartRequest createCart);
	public CartViewResponse updateCart(int id, int quantity);
	public void deleteCart(int id);
}
