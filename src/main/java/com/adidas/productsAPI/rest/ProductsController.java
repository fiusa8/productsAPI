package com.adidas.productsAPI.rest;

import com.adidas.productsAPI.dto.ProductDTO;
import com.adidas.productsAPI.mapper.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;


@RestController
@RequestMapping(value = "/products", produces = "application/hal+json")

public class ProductsController {


    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public Resources<ProductDTO> listOfProducts() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/hal+json");

        List<ProductDTO> allProducts = productRepository.findAll();
        Link link;
        for (ProductDTO product : allProducts) {
            System.out.println(product.get_Id());
            link = linkTo(ProductRepository.class).slash("products/" + product.get_Id()).withRel("delete");
            System.out.println(link);
            product.add(link);
            link = linkTo(ProductRepository.class).slash("products/" + product.get_Id()).withRel("edit");
            product.add(link);
        }

        List<Link> linksList = new ArrayList<>();
        linksList.add(linkTo(ProductRepository.class).slash("products").withRel("create"));
        //calculate offset
        linksList.add(linkTo(ProductRepository.class).slash("products?offset=15&limit=5").withSelfRel());
        linksList.add(linkTo(ProductRepository.class).slash("products?offset=10&limit=5").withRel("prev"));
        linksList.add(linkTo(ProductRepository.class).slash("products?offset=0&limit=5").withRel("first"));
        linksList.add(linkTo(ProductRepository.class).slash("products?offset=40&limit=5").withRel("last"));
        Resources<ProductDTO> result = new Resources<>(allProducts, linksList);
        return result;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Object> createNewProduct(@RequestBody ProductDTO product) {
        System.out.println(product);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/hal+json");

        ProductDTO productDTO = productRepository.save(product);
        System.out.println(productDTO);
        productDTO.add(new Link("/products/" + productDTO.get_Id()).withRel("edit"));
        productDTO.add(new Link("/products/" + productDTO.get_Id()).withRel("delete"));
        return new ResponseEntity<>(productDTO, headers, HttpStatus.CREATED);
    }

}