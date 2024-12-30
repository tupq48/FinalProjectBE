package com.app.final_project.cart;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.app.final_project.cart.dto.CartViewResponse;
import com.app.final_project.cart.dto.CreateCartRequest;
import com.app.final_project.cart.dto.UpdateCartRequest;
import com.app.final_project.common.PaginationRequest;
import com.app.final_project.common.PaginationResponse;
import com.app.final_project.product.Product;
import com.app.final_project.product.ProductRepository;
import com.app.final_project.user.User;
import com.app.final_project.user.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CartServiceImpl implements CartService{
	private final CartRepository cartRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final ModelMapper modelMapper;
	
	public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository, ModelMapper modelMapper) {
		this.cartRepository = cartRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<CartViewResponse> getAllCartsByUser(int userId) {
		return cartRepository.findAllByUserId(userId).stream()
				.map(cart -> modelMapper.map(cart, CartViewResponse.class))
				.collect(Collectors.toList());
	}

	@Override
	public CartViewResponse getCartById(int id) {
		Cart cart = cartRepository.findById(id).orElse(null);
    	return cart != null ? modelMapper.map(cart, CartViewResponse.class) : null;
	}

//	@Override
//	public PaginationResponse<Cart> getAllCartsPaginationByUser(int userId, PaginationRequest paginationRequest) {
//		Pageable pageable = PageRequest.of(
//	            paginationRequest.getPage() - 1,
//	            paginationRequest.getPageSize(),
//	            Sort.by(Sort.Direction.fromString(paginationRequest.getSortType()), paginationRequest.getSortBy())
//	        );
//        Page<Cart> cartPage = cartRepository.findByUserId(userId, pageable);
//        List<Cart> cartRelatedResponses = cartPage.getContent().stream()
//				.map(cart -> modelMapper.map(cart, CartViewResponse.class))
//              .collect(Collectors.toList());
//        return new PaginationResponse<>(
//            cartRelatedResponses,
//            cartPage.getNumber() + 1,
//            cartPage.getTotalPages(),
//            cartPage.getTotalElements(),
//            cartPage.getSize()
//        );
//	}

	@Override
	public CartViewResponse createCart(CreateCartRequest createCart) {
		int existsCart = cartRepository.existsByUserIdAndProductId(createCart.getUserId(), createCart.getProductId());
        if (existsCart == 1) {
            throw new RuntimeException("Product already exists in the cart.");
        }

    	Product product = productRepository.findById(createCart.getProductId())
    			.orElseThrow(() -> new RuntimeException("Product not found with id: " + createCart.getProductId()));
        User user = userRepository.findById(createCart.getUserId())
        		.orElseThrow(() -> new RuntimeException("User not found with id: " + createCart.getUserId()));

        if (product.getProductQuantity() < createCart.getQuantity()) {
            throw new RuntimeException("Invalid quantity with product ID: " + createCart.getProductId());
        }

        Cart cart = new Cart();
        cart.setQuantity(createCart.getQuantity());
        cart.setProduct(product);
        cart.setUser(user);
        Cart cartCreated = cartRepository.save(cart);

		return cartCreated != null ? modelMapper.map(cartCreated, CartViewResponse.class) : null;
    }

	@Override
	@Transactional
	public CartViewResponse updateCart(int id, UpdateCartRequest updateCart) {
		Cart cart = cartRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Cart not found with id: " + id));
		Product product = productRepository.findById(cart.getProduct().getProductId())
				.orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
		
		if (product.getProductQuantity() < updateCart.getQuantity()) {
			throw new RuntimeException("Invalid quantity with product ID: " + id);
	    }
		
        cart.setQuantity(updateCart.getQuantity());
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);

		return modelMapper.map(cart, CartViewResponse.class);
	}

	@Override
	public void deleteCart(int id) {
		cartRepository.deleteById(id);
	}

}
