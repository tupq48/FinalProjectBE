package com.app.final_project.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequest {
	private int page = 1;
    private int pageSize = 5;
    private String searchValue = ""; // search by name
    private String sortBy = "id";
    private String sortType = "asc";
}
