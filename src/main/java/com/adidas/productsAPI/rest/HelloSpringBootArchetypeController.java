package com.adidas.productsAPI.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloSpringBootArchetypeController {

    @RequestMapping(value = "/helloSpringBootArchetype", method = RequestMethod.GET)
    public ResponseEntity<String> helloArchetype(){
        return new ResponseEntity<>("Welcome to the SpringBoot archetype controller.", HttpStatus.OK);
    }
}
