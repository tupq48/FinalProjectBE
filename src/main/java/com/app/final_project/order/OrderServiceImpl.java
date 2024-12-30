package com.app.final_project.order;

import java.util.ArrayList;
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

import com.app.final_project.cart.Cart;
import com.app.final_project.cart.CartRepository;
import com.app.final_project.common.PaginationRequest;
import com.app.final_project.common.PaginationResponse;
import com.app.final_project.order.dto.CreateOrderByCartRequest;
import com.app.final_project.order.dto.OrderViewResponse;
import com.app.final_project.order.dto.UpdateOrderRequest;
import com.app.final_project.orderDetail.OrderDetail;
import com.app.final_project.product.Product;
import com.app.final_project.product.ProductRepository;
import com.app.final_project.user.User;
import com.app.final_project.user.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService{
	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final CartRepository cartRepository;
	private final ModelMapper modelMapper;

	public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository,
			ProductRepository productRepository, CartRepository cartRepository, ModelMapper modelMapper) {
		this.orderRepository = orderRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.cartRepository = cartRepository;
		this.modelMapper = modelMapper;
	}
	
	@Override
	public List<OrderViewResponse> getAllOrders() {
		return orderRepository.findAll().stream()
				.map(order -> modelMapper.map(order, OrderViewResponse.class))
				.collect(Collectors.toList());
	}
	
	@Override
	public List<OrderViewResponse> getAllOrdersByUser(int userId) {
		return orderRepository.findAllByUserId(userId).stream()
				.map(order -> modelMapper.map(order, OrderViewResponse.class))
				.collect(Collectors.toList());
	}

	@Override
	public OrderViewResponse getOrderById(int id) {
		Order order = orderRepository.findById(id).orElse(null);
    	return order != null ? modelMapper.map(order, OrderViewResponse.class) : null;
	}
	
//	@Override
//	public PaginationResponse<Order> getAllOrdersByUser(int userId, PaginationRequest paginationRequest) {
//		Pageable pageable = PageRequest.of(
//	            paginationRequest.getPage() - 1,
//	            paginationRequest.getPageSize(),
//	            Sort.by(Sort.Direction.fromString(paginationRequest.getSortType()), paginationRequest.getSortBy())
//	        );
//	        Page<Order> orderPage = orderRepository.findByUserId(userId, pageable);
//	        List<Order> orderRelatedResponses = orderPage.getContent().stream()
//	            .collect(Collectors.toList());
//	        
//	        return new PaginationResponse<>(
//	    		orderRelatedResponses,
//	    		orderPage.getNumber() + 1,
//	            orderPage.getTotalPages(),
//	            orderPage.getTotalElements(),
//	            orderPage.getSize()
//	        );
//	}

	@Override
	public OrderViewResponse createOrderByCart(CreateOrderByCartRequest createOrder) {
		Order order = new Order();
    	order.setDescription(createOrder.getDescription());
        order.setAddress(createOrder.getAddress());
        order.setPhoneNumber(createOrder.getPhoneNumber());
    	
        User user = userRepository.findById(createOrder.getUserId())
        		.orElseThrow(() -> new RuntimeException("User not found with id: " + createOrder.getUserId()));
        order.setUser(user);
        
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (Integer cartId : createOrder.getCartIds()) {
            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(() -> new RuntimeException("Cart not found with id: " + cartId));
        
            Product product = cart.getProduct(); 
            if (product.getProductQuantity() < cart.getQuantity()) {
                throw new RuntimeException("Invalid quantity with product ID: " + product.getProductId());
            }

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setQuantity(cart.getQuantity());
            orderDetail.setPrice(cart.getProduct().getProductPrice());
            orderDetail.setProduct(product);
            orderDetail.setOrder(order);
            orderDetails.add(orderDetail);
            product.setProductQuantity(product.getProductQuantity() - cart.getQuantity());
            order.setTotalPrice(order.getTotalPrice() + cart.getQuantity()*cart.getProduct().getProductPrice());
            
            cartRepository.deleteById(cartId);
            productRepository.save(product);
        }
        order.setOrderDetails(orderDetails);
        Order orderCreated = orderRepository.save(order);

		return orderCreated != null ? modelMapper.map(orderCreated, OrderViewResponse.class) : null;
	}
	
//	@Override
//	@Transactional
//	public Order updateOrder(int id, UpdateOrderRequest updateOrder) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public void deleteOrder(int id) {
		Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setIsDeleted(true);
            orderRepository.save(order);
        } else {        	
        	throw new RuntimeException("Order not found with id: " + id);
        }
		
	}

}
