package com.adidas.productsAPI.rest;

import com.adidas.productsAPI.dto.ProductDTO;
import com.adidas.productsAPI.service.ProductService;
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
    private ProductService productService;

    @GetMapping
    public Resources<ProductDTO> listOfProducts() {
        List<ProductDTO> allProducts = productService.findAll();
        for (ProductDTO product : allProducts) {
            product.add(new Link("/" + product.getProductId()).withRel("delete"));
            product.add(new Link("/" + product.getProductId()).withRel("edit"));
        }

        List<Link> linksList = new ArrayList<>();
        linksList.add(new Link("/").withRel("create"));
        //calculate offset
        linksList.add(new Link("/?offset=15&limit=5").withSelfRel());
        linksList.add(new Link("/?offset=20&limit=5").withRel("next"));
        linksList.add(new Link("/?offset=10&limit=5").withRel("prev"));
        linksList.add(new Link("/?offset=0&limit=5").withRel("first"));
        linksList.add(new Link("/?offset=40&limit=5").withRel("last"));
        Resources<ProductDTO> result = new Resources<>(allProducts, linksList);
        return result;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Object> createNewProduct(@RequestBody ProductDTO product) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/hal+json");

        ProductDTO productDTO = productService.saveProduct(product);
        productDTO.add(new Link("/" + productDTO.getProductId()).withRel("edit"));
        productDTO.add(new Link("/" + productDTO.getProductId()).withRel("delete"));
        return new ResponseEntity<>(productDTO, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{product_id}", method = RequestMethod.GET)
    public HttpEntity<ProductDTO> getProductById(@PathVariable("product_id") String productId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/hal+json");

        Optional<ProductDTO> productDTO = productService.findByProductId(productId);
        if (!productDTO.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            ProductDTO product = productDTO.get();
            product.add(new Link("/" + product.getProductId()).withRel("delete"));
            product.add(new Link("/" + product.getProductId()).withRel("edit"));
            return new ResponseEntity<>(product, headers, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{product_id}", method = RequestMethod.PATCH)
    public HttpEntity<ProductDTO> updateProduct(@PathVariable("product_id") String productId,
                                                @RequestBody ProductDTO productBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/hal+json");

        Optional<ProductDTO> productDTO = productService.findByProductId(productId);
        if(!productDTO.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            ProductDTO product = productDTO.get();
            product.setName(productBody.getName());
            product.setSize(productBody.getSize());
            product.setPrice(productBody.getPrice());
            productService.updateProduct(product);
            product.add(new Link("/" + product.getProductId()).withRel("delete"));
            product.add(new Link("/" + product.getProductId()).withRel("edit"));
            return new ResponseEntity<>(product, headers, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{product_id}", method = RequestMethod.DELETE)
    public HttpEntity<ProductDTO> getDeleteProduct(@PathVariable("product_id") String productId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/hal+json");

        productService.deleteByProductId(productId);
        return new ResponseEntity<>(null, headers, HttpStatus.NO_CONTENT);
    }

}