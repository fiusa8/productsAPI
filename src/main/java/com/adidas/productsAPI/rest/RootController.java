package com.adidas.productsAPI.rest;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")

public class RootController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Object> retrieveApiRoot() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/hal+json");

        ResourceSupport response = new ResourceSupport();
        response.add(new Link("/"));
        response.add(new Link("/pro").withRel("products"));

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

}
