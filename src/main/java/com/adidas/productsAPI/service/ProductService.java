package com.adidas.productsAPI.service;

import com.adidas.productsAPI.dto.ProductDTO;

import java.util.List;
import java.util.Optional;


public interface ProductService {
    ProductDTO saveProduct(ProductDTO product);

    Optional<ProductDTO> findByProductId(String productId);

    void deleteByProductId(String productId);

    void updateProduct(ProductDTO product);

    boolean ProductExists(ProductDTO product);

    List<ProductDTO> findAll();
}