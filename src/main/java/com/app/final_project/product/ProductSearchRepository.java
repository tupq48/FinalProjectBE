package com.app.final_project.product;

import com.app.final_project.product.document.ProductES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductES, Integer> {
    List<ProductES> findByProductNameContaining(String keyword);

}
