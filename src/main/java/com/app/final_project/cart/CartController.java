package com.app.final_project.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.final_project.cart.dto.CartViewResponse;
import com.app.final_project.cart.dto.CreateCartRequest;
import com.app.final_project.cart.dto.UpdateCartRequest;
import com.app.final_project.common.PaginationRequest;
import com.app.final_project.common.PaginationResponse;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	private final CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getAllCartsByUser(@PathVariable("userId") int userId) {
		try {
			var carts = cartService.getAllCartsByUser(userId);
			return ResponseEntity.ok(carts);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getCartById(@PathVariable("id") int id) {
		try {
			var cart = cartService.getCartById(id);
			return ResponseEntity.ok(cart);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

//	@GetMapping("/user/{userId}")
//    public ResponseEntity<?> getAllCartsPaginationByUser(@PathVariable("userId") int userId, PaginationRequest paginationRequest) {
//
//        PaginationResponse<Cart> cartResponses = cartService.getAllCartsPaginationByUser(userId, paginationRequest);
//        return new ResponseEntity<>(cartResponses, HttpStatus.OK);
//    }

	@PostMapping()
	public ResponseEntity<?> createCart(CreateCartRequest createCart) {
		try {
			var cart = cartService.createCart(createCart);
			return ResponseEntity.ok(cart);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCart(@PathVariable("id") int id, UpdateCartRequest updateCartRequest) {
    	try
		{
    		var updatedCart = cartService.updateCart(id, updateCartRequest);
            return ResponseEntity.ok(updatedCart);
		}
		catch (Exception ex)
		{
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}

    }

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCart(@PathVariable("id") int id) {
		try {
			cartService.deleteCart(id);
			return ResponseEntity.ok(true);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
