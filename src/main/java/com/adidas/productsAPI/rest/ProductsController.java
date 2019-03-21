package com.adidas.productsAPI.rest;

import com.adidas.productsAPI.dto.ProductDTO;
import com.adidas.productsAPI.mapper.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/")

public class ProductsController {


    @Autowired
    private ProductRepository productRepository;

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public ResponseEntity<Object> listOfProducts() {
        for (ProductDTO productDTO : productRepository.findAll()) {
            System.out.println(productDTO.toString());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/hal+json");
        ResourceSupport response = new ResourceSupport();
        response.add(new Link("/").withRel("create"));
        //calcular offset
        response.add(new Link("/?offset=15&limit=5").withRel("self"));
        response.add(new Link("/?offset=20&limit=5").withRel("next"));
        response.add(new Link("/?offset=10&limit=5").withRel("prev"));
        response.add(new Link("/?offset=0&limit=5").withRel("first"));
        response.add(new Link("/?offset=40&limit=5").withRel("last"));

        ProductDTO p1 = new ProductDTO("Apfelstrudel", "m", 120);
        ProductDTO p2 = new ProductDTO("Apfelstrudel2", "l", 120);
        Resource<ProductDTO> r1 = new Resource<>(p1, new Link("http://example.com/products/1"));
        Resource<ProductDTO> r2 = new Resource<>(p1, new Link("http://example.com/products/1"));
        List<Link> linksList = new ArrayList<>();
        linksList.add(new Link("/?offset=15&limit=5").withRel("self"));
        linksList.add(new Link("/?offset=15&limit=4").withRel("sel"));
        linksList.add(new Link("/?offset=15&limit=6").withRel("selfe"));
        Links links = new Links(linksList);
        Resources<Resource<ProductDTO>> resources = new Resources(Arrays.asList(r1, r2), links);
        return new ResponseEntity<>(resources, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    public ResponseEntity<Object> createNewProduct() {
        return new ResponseEntity<>(("Succesful"), HttpStatus.CREATED);
    }

}