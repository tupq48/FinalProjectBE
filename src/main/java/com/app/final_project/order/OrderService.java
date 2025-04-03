package com.app.final_project.order;

import java.util.List;

import com.app.final_project.common.PaginationRequest;
import com.app.final_project.common.PaginationResponse;
import com.app.final_project.order.dto.CreateOrderByCartRequest;
import com.app.final_project.order.dto.OrderViewResponse;
import com.app.final_project.order.dto.UpdateOrderRequest;

public interface OrderService {
	public List<OrderViewResponse> getAllOrders();
	public List<OrderViewResponse> getAllOrdersByUser(int userId);
	public OrderViewResponse getOrderById(int id);
//	public PaginationResponse<Order> getAllOrdersByUser(int userId, PaginationRequest paginationRequest);
	public OrderViewResponse createOrderByCart(CreateOrderByCartRequest createOrder);
//	public OrderViewResponse updateOrder(int id, UpdateOrderRequest updateOrder);
	public void deleteOrder(int id);
}
