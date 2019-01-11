package com.agitech.packageservice.core.products;

import org.springframework.web.client.RestTemplate;

import java.util.Set;
import java.util.stream.Collectors;

public class ProductServiceRest implements ProductService {

    private static final String PRODUCT_SERVICE_ENDPOINT = "/products/{id}";
    private final RestTemplate restTemplate;

    public ProductServiceRest(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getProduct(final String id) {
        return this.restTemplate.getForObject(PRODUCT_SERVICE_ENDPOINT, Product.class, id);
    }

    @Override
    public Set<Product> getProducts(final Set<String> id) {

        // We currently retrieve each product individually. If we do not expect the no of produces to grow too
        // large we could just retrieve the full list!
        return id.stream()
                .map(this::getProduct)
                .collect(Collectors.toSet());
    }
}
