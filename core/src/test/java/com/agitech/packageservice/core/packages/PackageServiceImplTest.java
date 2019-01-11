package com.agitech.packageservice.core.packages;

import com.agitech.packageservice.core.exchangerates.ExchangeRatesService;
import com.agitech.packageservice.core.packages.persistence.PackageEntity;
import com.agitech.packageservice.core.packages.persistence.PackageRepository;
import com.agitech.packageservice.core.products.Product;
import com.agitech.packageservice.core.products.ProductService;
import com.agitech.packageservice.core.utils.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

import static java.util.Collections.singleton;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(MockitoJUnitRunner.class)
public class PackageServiceImplTest {

    @Mock
    private PackageRepository packageRepository;

    @Mock
    private ProductService productService;

    @Mock
    private ExchangeRatesService exchangeRatesService;

    private PackageServiceImpl packageService;

    @Before
    public void setUp()  {
        packageService = new PackageServiceImpl(packageRepository, productService, exchangeRatesService);
    }

    @Test
    public void createOrUpdatePackageWillSavePackage() {

        // Given
        final Package newPackage = new Package.Builder("Test Package")
                .withDescription("Test description")
                .withProducts(singleton("ABCD"))
                .build();

        // When
        packageService.createOrUpdatePackage(newPackage);

        // Then
        then(productService).should().getProducts(singleton("ABCD"));

        final ArgumentCaptor<PackageEntity> captor = ArgumentCaptor.forClass(PackageEntity.class);
        then(packageRepository).should().save(captor.capture());
        assertThat(captor.getValue().getId(), is(nullValue()));
        assertThat(captor.getValue().getName(), is("Test Package"));
        assertThat(captor.getValue().getDescription(), is("Test description"));
        assertThat(captor.getValue().getProducts(), contains("ABCD"));
    }

    @Test
    public void deletePackageWillDeleteById() {

        // When
        packageService.deletePackage(1L);

        // Then
        then(packageRepository).should().deleteById(1L);
    }

    @Test
    public void retrievePackageWillLoadPackageAndTotalProductAmount() {

        // Given
        given(packageRepository.findById(1L))
                .willReturn(Optional.of(
                        new PackageEntity(1L, "Test Package", "Test description", singleton("ABCD"))));

        given(productService.getProducts(singleton("ABCD")))
                .willReturn(singleton(new Product("Test Package", "Test description", 650)));

        // When
        final Optional<Package> result = packageService.retrievePackage(1L, Currency.getInstance("USD"));

        // Then
        final Package expectedPackage = new Package.Builder("Test Package")
                .withId(1L)
                .withDescription("Test description")
                .withProducts(singleton("ABCD"))
                .withTotalPrice(new Money(Currency.getInstance("USD"), 650))
                .build();

        assertThat(result, is(Optional.of(expectedPackage)));
    }


    @Test
    public void retrievePackageWillLoadPackageAndConvertCurrency() {

        // Given
        given(packageRepository.findById(1L))
                .willReturn(Optional.of(
                        new PackageEntity(1L, "Test Package", "Test description", singleton("ABCD"))));

        given(productService.getProducts(singleton("ABCD")))
                .willReturn(singleton(new Product("Test Package", "Test description", 650)));

        given(exchangeRatesService.getLatestRate(Currency.getInstance("USD"), Currency.getInstance("GBP")))
                .willReturn(new BigDecimal("0.75"));

        // When
        final Optional<Package> result = packageService.retrievePackage(1L, Currency.getInstance("GBP"));

        // Then
        final Package expectedPackage = new Package.Builder("Test Package")
                .withId(1L)
                .withDescription("Test description")
                .withProducts(singleton("ABCD"))
                .withTotalPrice(new Money(Currency.getInstance("GBP"), 488))
                .build();

        assertThat(result, is(Optional.of(expectedPackage)));
    }
}