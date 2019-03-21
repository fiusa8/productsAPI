package com.adidas.productsAPI.dto;

import org.springframework.data.annotation.Id;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@Relation(collectionRelation = "products")
public class ProductDTO extends ResourceSupport{

    @Id
    public String productId;
    public String name;
    public String size;
    public double price;

    public ProductDTO() { }

    public ProductDTO(String name, String size, double price) {
        this.name = name;
        this.size = size;
        this.price = price;
    }

    public String getProductId(){
        return this.productId;
    }
}
