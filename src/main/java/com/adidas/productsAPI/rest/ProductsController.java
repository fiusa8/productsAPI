package com.adidas.productsAPI.rest;

import com.adidas.productsAPI.dto.ProductDTO;
import com.adidas.productsAPI.mapper.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


@RestController
@RequestMapping(value = "/products", produces = "application/hal+json")

public class ProductsController {


    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public Resources<ProductDTO> listOfProducts() {
        //HttpHeaders headers = new HttpHeaders();
        //headers.add("Content-Type", "application/hal+json");

        List<ProductDTO> allProducts = productRepository.findAll();
        Link link;
        for (ProductDTO product : allProducts) {
            link = linkTo(ProductRepository.class).slash("products/" + product.getProductId()).withRel("delete");
            product.add(link);
            link = linkTo(ProductRepository.class).slash("products/" + product.getProductId()).withRel("edit");
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
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/hal+json");

        ProductDTO productDTO = productRepository.save(product);
        productDTO.add(new Link("/products/" + productDTO.getProductId()).withRel("edit"));
        productDTO.add(new Link("/products/" + productDTO.getProductId()).withRel("delete"));
        return new ResponseEntity<>(productDTO, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{product_id}", method = RequestMethod.GET)
    public HttpEntity<ProductDTO> getProductById(@PathVariable("product_id") String productId) {
        Optional<ProductDTO> productDTO = productRepository.findById(productId);
        if (!productDTO.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            ProductDTO product = productDTO.get();
            product.add(linkTo(methodOn(ProductsController.class).getProductById(product.getProductId())).withRel("edit"));
            product.add(linkTo(methodOn(ProductsController.class).getProductById(product.getProductId())).withRel("delete"));
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{product_id}", method = RequestMethod.PATCH)
    public HttpEntity<ProductDTO> updateProduct(@PathVariable("product_id") String productId) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @RequestMapping(value = "/{product_id}", method = RequestMethod.DELETE)
    public HttpEntity<ProductDTO> getDeleteProduct(@PathVariable("product_id") String productId) {
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

}