package com.adidas.productsAPI.service;

import com.adidas.productsAPI.dto.ProductDTO;
import com.adidas.productsAPI.mapper.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductDTO saveProduct(ProductDTO product){
        return productRepository.save(product);
    }

    @Override
    public Optional<ProductDTO> findByProductId(String productId){
        return productRepository.findById(productId);
    }

    @Override
    public void deleteByProductId(String productId){
        productRepository.deleteById(productId);
    }

    @Override
    public void updateProduct(ProductDTO product){
        productRepository.save(product);
    }

    @Override
    public boolean ProductExists(ProductDTO product){
        return productRepository.exists(Example.of(product));
    }

    @Override
    public List<ProductDTO> findAll(){
        return productRepository.findAll();
    }
}
