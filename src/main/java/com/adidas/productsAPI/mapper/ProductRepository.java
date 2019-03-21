package com.adidas.productsAPI.mapper;

import com.adidas.productsAPI.dto.ProductDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "products")
public interface ProductRepository extends MongoRepository<ProductDTO, String> { }

