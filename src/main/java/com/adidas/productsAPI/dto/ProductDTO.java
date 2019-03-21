package com.adidas.productsAPI.dto;

import org.springframework.data.annotation.Id;
import org.springframework.hateoas.ResourceSupport;

public class ProductDTO extends ResourceSupport {

    @Id
    public String id;

    public String name;
    public String size;
    public double price;

    public ProductDTO() { }

    public ProductDTO(String name, String size, double price) {
        this.name = name;
        this.size = size;
        this.price = price;
    }

    public String toString() {
        return ("Nombre: " + this.name + ", talla: " + this.size + ", price: " + this.price + "$");
    }
}
