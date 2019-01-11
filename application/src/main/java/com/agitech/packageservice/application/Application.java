package com.agitech.packageservice.application;

import com.agitech.packageservice.core.CoreConfig;
import com.agitech.packageservice.core.exchangerates.ExchangeRatesService;
import com.agitech.packageservice.core.exchangerates.ExchangeRatesServiceRest;
import com.agitech.packageservice.core.packages.PackageService;
import com.agitech.packageservice.core.packages.PackageServiceImpl;
import com.agitech.packageservice.core.packages.persistence.PackageRepository;
import com.agitech.packageservice.core.products.ProductService;
import com.agitech.packageservice.core.products.ProductServiceRest;
import com.agitech.packageservice.core.utils.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;

@SpringBootApplication(scanBasePackageClasses = {Application.class, CoreConfig.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public RestTemplate productServiceRestTemplate(
            final @Value("${product.service.url}") String url,
            final @Value("${product.service.username}") String username,
            final @Value("${product.service.password}") String password) {

        return new RestTemplateBuilder()
                .basicAuthentication(username, password)
                .rootUri(url)
                .errorHandler(new ErrorHandler())
                .build();
    }

    @Bean
    public ProductService productService(final RestTemplate productServiceRestTemplate) {
        return new ProductServiceRest(productServiceRestTemplate);
    }

    @Bean
    public RestTemplate exchangeRatesServiceRestTemplate(final @Value("${fixer.io.url}") String url) {

        return new RestTemplateBuilder()
                .rootUri(url)
                .errorHandler(new ErrorHandler())
                .build();
    }

    @Bean
    public ExchangeRatesService exchangeRatesService(
            final RestTemplate exchangeRatesServiceRestTemplate,
            final @Value("${fixer.io.apikey}") String apiKey) {

        return new ExchangeRatesServiceRest(exchangeRatesServiceRestTemplate, apiKey);
    }

    @Bean
    public PackageService packageService(final PackageRepository packageRepository,
                                         final ProductService productService,
                                         final ExchangeRatesService exchangeRatesService) {
        return new PackageServiceImpl(packageRepository, productService, exchangeRatesService);
    }

    private static class ErrorHandler implements ResponseErrorHandler {

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return (response.getStatusCode().series() == CLIENT_ERROR);
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {

             if (response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
                if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                    throw new NotFoundException();
                }
            }
        }
    }
}
