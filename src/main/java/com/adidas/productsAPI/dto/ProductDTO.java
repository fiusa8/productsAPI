package com.adidas.productsAPI.dto;

import org.springframework.data.annotation.Id;
import org.springframework.hateoas.ResourceSupport;


public class ProductDTO extends ResourceSupport{


    public String id;
    public String name;
    public String size;
    public double price;

    public ProductDTO() { }

    public ProductDTO(String id, String name, String size, double price) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.price = price;
    }

    public String toString() {
        return ("Id: " + this.id +  " Nombre: " + this.name + ", talla: " + this.size + ", price: " + this.price + "$");
    }

    public String get_Id(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getSize(){
        return this.size;
    }

    public double getPrice(){
        return this.price;
    }
}
