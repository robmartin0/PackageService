package com.agitech.packageservice.core.products;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceRestTest {

    private static final String EXPECTED_URL = "/products/{id}";

    @Mock
    private RestTemplate restTemplate;

    private ProductServiceRest productServiceRest;

    @Before
    public void setUp() {
        productServiceRest = new ProductServiceRest(restTemplate);
    }

    @Test
    public void getProductWillRetrieveProduct() {

        // Given
        final Product product = new Product("VqKb4tyj9V6i", "Shield", 1149);

        given(restTemplate.getForObject(EXPECTED_URL, Product.class, "VqKb4tyj9V6i"))
                .willReturn(product);

        // When
        final Product result = productServiceRest.getProduct("VqKb4tyj9V6i");

        // Then
        assertThat(result, is(product));
    }

    @Test
    public void getProductsWillRetrieveAllProducts() {

        // Given
        final Product product1 = new Product("VqKb4tyj9V6i", "Shield", 1149);
        final Product product2 = new Product("DXSQpv6XVeJm", "Helmet", 999);

        given(restTemplate.getForObject(EXPECTED_URL, Product.class, "VqKb4tyj9V6i"))
                .willReturn(product1);
        given(restTemplate.getForObject(EXPECTED_URL, Product.class, "DXSQpv6XVeJm"))
                .willReturn(product2);

        // When
        final Set<String> productIds = new HashSet<>();
        productIds.add("VqKb4tyj9V6i");
        productIds.add("DXSQpv6XVeJm");
        final Set<Product> result = productServiceRest.getProducts(productIds);

        // Then
        assertThat(result, containsInAnyOrder(product1, product2));
    }
}