package com.adidas.productsAPI;

import com.adidas.productsAPI.dto.ProductDTO;
import com.adidas.productsAPI.rest.ProductsController;
import com.adidas.productsAPI.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductsController.class)

public class ProductsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void listOfProducts() throws Exception {

        ProductDTO productDTO = new ProductDTO("Hoodie", "M", 80);
        productDTO.setProductId("235");
        given(productService.findAll()).willReturn( //
                Arrays.asList(productDTO));

        mvc.perform(get("/products").accept(MediaTypes.HAL_JSON_VALUE)) //
                .andDo(print()) //
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.create.href", is("/")))
                .andExpect(jsonPath("$._links.self.href", is("/?offset=15&limit=5")))
                .andExpect(jsonPath("$._links.next.href", is("/?offset=20&limit=5")))
                .andExpect(jsonPath("$._links.prev.href", is("/?offset=10&limit=5")))
                .andExpect(jsonPath("$._links.first.href", is("/?offset=0&limit=5")))
                .andExpect(jsonPath("$._links.last.href", is("/?offset=40&limit=5")))
                .andExpect(jsonPath("$._embedded.products[0]._links.edit.href", is("/235" )))
                .andExpect(jsonPath("$._embedded.products[0]._links.delete.href", is("/235" )))
                .andExpect(jsonPath("$._embedded.products[0].productId", is("235")))
                .andExpect(jsonPath("$._embedded.products[0].name", is("Hoodie" )))
                .andExpect(jsonPath("$._embedded.products[0].size", is("M")))
                .andExpect(jsonPath("$._embedded.products[0].price", is(80.0)));
    }

    @Test
    public void createNewProduct() throws Exception {

        ProductDTO productDTO = new ProductDTO("Hoodie Originals", "L", 60);
        productDTO.setProductId("236");
        given(productService.saveProduct(productDTO)).willReturn(productDTO);

        JSONObject jo = new JSONObject();
        jo.put("name", productDTO.name);
        jo.put("size", productDTO.size);
        jo.put("price", productDTO.price);

        mvc.perform(post("/products")
                    .accept(MediaTypes.HAL_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productDTO)))
                .andDo(print()) //
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId", is("236")))
                .andExpect(jsonPath("$.name", is("Hoodie Originals" )))
                .andExpect(jsonPath("$.size", is("L")))
                .andExpect(jsonPath("$.price", is(60.0)));
    }

    @Test
    public void getProductById() throws Exception {
        ProductDTO productDTO = new ProductDTO("Hoodie", "M", 80);
        productDTO.setProductId("235");
        given(productService.findByProductId("235")).willReturn(Optional.of(productDTO));

        mvc.perform(get("/products/{product_id}", "235")
                    .accept(MediaTypes.HAL_JSON_VALUE))
                .andDo(print()) //
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.edit.href", is("/235" )))
                .andExpect(jsonPath("$._links.delete.href", is("/235" )))
                .andExpect(jsonPath("$.productId", is("235")))
                .andExpect(jsonPath("$.name", is("Hoodie")))
                .andExpect(jsonPath("$.size", is("M")))
                .andExpect(jsonPath("$.price", is(80.0)));
    }

    @Test
    public void updateProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO("Hoodie", "M", 80);
        productDTO.setProductId("235");
        given(productService.findByProductId("235")).willReturn(Optional.of(productDTO));

        mvc.perform(patch("/products/{product_id}", "235")
                    .accept(MediaTypes.HAL_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productDTO)))
                .andDo(print()) //
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.edit.href", is("/235" )))
                .andExpect(jsonPath("$._links.delete.href", is("/235" )))
                .andExpect(jsonPath("$.productId", is("235")))
                .andExpect(jsonPath("$.name", is("Hoodie")))
                .andExpect(jsonPath("$.size", is("M")))
                .andExpect(jsonPath("$.price", is(80.0)));
    }

    /*@Test
    public void deleteProduct() throws Exception {
        given(productService.deleteByProductId("235"));

        mvc.perform(patch("/products/{product_id}", "235")
                    .accept(MediaTypes.HAL_JSON_VALUE))
                .andDo(print()) //
                .andExpect(status().isNoContent());
    }*/
}