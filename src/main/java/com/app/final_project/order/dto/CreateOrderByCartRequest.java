package com.app.final_project.order.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderByCartRequest {
	private String description;
	private String address;
	private String phoneNumber;
	
	private int userId;
	private List<Integer> cartIds;
}
