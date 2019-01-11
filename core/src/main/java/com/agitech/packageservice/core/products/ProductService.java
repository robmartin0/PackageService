package com.agitech.packageservice.core.products;

import com.agitech.packageservice.core.utils.NotFoundException;

import java.util.Set;

/**
 * Service responsible for retrieving the state of products
 */
public interface ProductService {

    Product getProduct(String id) throws NotFoundException;
    Set<Product> getProducts(Set<String> id) throws NotFoundException;
}
