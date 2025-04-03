package com.app.final_project.order;

import org.mortbay.log.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.final_project.common.PaginationRequest;
import com.app.final_project.common.PaginationResponse;
import com.app.final_project.order.dto.CreateOrderByCartRequest;

@RestController
@RequestMapping("api/order")
public class OrderController {
	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@GetMapping
	public ResponseEntity<?> getAllOrders() {
		try {
			var orders = orderService.getAllOrders();
			return ResponseEntity.ok(orders);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getAllOrdersByUser(@PathVariable("userId") int userId) {
		try {
			var orders = orderService.getAllOrdersByUser(userId);
			return ResponseEntity.ok(orders);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getOrderById(@PathVariable("id") int id) {
		try {
			var order = orderService.getOrderById(id);
			return ResponseEntity.ok(order);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

//	@GetMapping("/user/{userId}")
//    public ResponseEntity<?> getOrdersByUser(@PathVariable("userId") int userId, PaginationRequest paginationRequest) {
//
//        PaginationResponse<OrderViewResponse> OrderResponses = orderService.getAllOrdersByUser(userId, paginationRequest);
//        return new ResponseEntity<>(OrderResponses, HttpStatus.OK);
//    }

	@PostMapping
	public ResponseEntity<?> createOrder(@RequestBody CreateOrderByCartRequest createOrder) {
		try {
			var order = orderService.createOrderByCart(createOrder);
			return ResponseEntity.ok(order);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateOrder(@PathVariable("id") int id, @RequestBody UpdateOrderRequest updateOrderRequest) {
//    	try
//		{
//    		OrderRelatedResponse updatedOrder = orderService.updateOrder(id, updateOrderRequest);
//            return ResponseEntity.ok(updatedOrder);
//		}
//		catch (Exception ex)
//		{
//			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
//		}
//
//    }

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOrder(@PathVariable("id") int id) {
		try {
			orderService.deleteOrder(id);
			return ResponseEntity.ok(true);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().build();
		}
	}
}
