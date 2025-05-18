package com.example.product_service.service;

import com.example.product_service.dto.ProductRequest;
import com.example.product_service.dto.ProductResponse;
import com.example.product_service.model.Product;
import com.example.product_service.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        ProductResponse productResponse = new ProductResponse(product.getId(),product.getName(),product.getDescription(),product.getPrice());

        productRepository.save(product);
        log.info("Product Created Successfully");

        return productResponse;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> product = productRepository.findAll();
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product p : product) {
            ProductResponse response = new ProductResponse();
            response.setId(p.getId());
            response.setName(p.getName());
            response.setDescription(p.getDescription());
            response.setPrice(p.getPrice());

            productResponses.add(response);
        }
        return productResponses;
    }
}
