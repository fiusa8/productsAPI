package com.adidas.productsAPI.service;

import com.adidas.productsAPI.dto.ProductDTO;

import java.util.List;
import java.util.Optional;


public interface ProductService {
    ProductDTO saveProduct(ProductDTO e);

    Optional<ProductDTO> findByProductId(String productId);

    void deleteByProductId(ProductDTO productId);

    void updateProduct(ProductDTO e);

    boolean ProductExists(ProductDTO e);

    List<ProductDTO> findAll();
}