package com.adidas.productsAPI.mapper;

import com.adidas.productsAPI.dto.ProductDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "product", path = "product")

public interface ProductRepository extends MongoRepository<ProductDTO, String> {

    Optional<ProductDTO> findById (String id);

    List<ProductDTO> findByName(String name);

    List<ProductDTO> findAll();
}

