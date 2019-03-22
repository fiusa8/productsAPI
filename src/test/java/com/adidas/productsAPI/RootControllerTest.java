package com.adidas.productsAPI;

import com.adidas.productsAPI.dto.ProductDTO;
import com.adidas.productsAPI.mapper.ProductRepository;
import com.adidas.productsAPI.rest.RootController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RootController.class)

public class RootControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductRepository repository;

    @Test
    public void retrieveApiRoot() throws Exception {
        mvc.perform(get("/").accept(MediaTypes.HAL_JSON_VALUE)) //
                .andDo(print()) //
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href", is("/")))
                .andExpect(jsonPath("$._links.products.href", is("/products")));
    }
}
