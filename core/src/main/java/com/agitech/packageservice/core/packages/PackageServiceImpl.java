package com.agitech.packageservice.core.packages;

import com.agitech.packageservice.core.exchangerates.ExchangeRatesService;
import com.agitech.packageservice.core.packages.persistence.PackageEntity;
import com.agitech.packageservice.core.packages.persistence.PackageRepository;
import com.agitech.packageservice.core.products.Product;
import com.agitech.packageservice.core.products.ProductService;
import com.agitech.packageservice.core.utils.Money;
import com.agitech.packageservice.core.utils.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PackageServiceImpl implements PackageService {

    private static final Logger LOG = LoggerFactory.getLogger(PackageServiceImpl.class);

    private static final Currency DEFAULT_CURRENCY = Currency.getInstance("USD");

    private final PackageRepository packageRepository;
    private final ProductService productService;
    private final ExchangeRatesService exchangeRatesService;

    public PackageServiceImpl(final PackageRepository packageRepository,
                              final ProductService productService,
                              final ExchangeRatesService exchangeRatesService) {
        this.packageRepository = packageRepository;
        this.productService = productService;
        this.exchangeRatesService = exchangeRatesService;
    }

    @Override
    public void createOrUpdatePackage(final Package newPackage) {
        LOG.debug("Creating or updating package {}", newPackage);

        try {
            productService.getProducts(newPackage.getProducts());
        } catch (final NotFoundException e) {
            throw new IllegalArgumentException("Unrecognized product");
        }

        final PackageEntity packageEntity = PackageEntity.fromPackage(newPackage);
        packageRepository.save(packageEntity);
    }

    @Override
    public void deletePackage(final long packageId) {
        LOG.debug("deleting package {}", packageId);
        packageRepository.deleteById(packageId);
    }

    @Override
    public Optional<Package> retrievePackage(final long packageId, final Currency currency) {
        LOG.debug("Retrieving package {} with currency {}", packageId, currency);
        return packageRepository.findById(packageId)
                .map(packageEntity -> toPackage(packageEntity, currency));
    }

    @Override
    public Set<Package> retrieveAllPackages(final Currency currency) {
        LOG.debug("Retrieving all packages");
        final Iterable<PackageEntity> allPackages = packageRepository.findAll();
        return StreamSupport.stream(allPackages.spliterator(), false)
                .map(packageEntity -> toPackage(packageEntity, currency))
                .collect(Collectors.toSet());
    }

    private Package toPackage(final PackageEntity packageEntity, final Currency currency) {
        final Set<String> productsIds = packageEntity.getProducts();
        final Set<Product> products = productService.getProducts(productsIds);

        final long totalPriceInUsd = products.stream()
                .mapToLong(Product::getUsdPrice)
                .sum();

        final long totalPriceInCurrency = currency.equals(DEFAULT_CURRENCY)
                ? totalPriceInUsd
                : convertToCurrency(currency, totalPriceInUsd);

        return new Package.Builder(packageEntity.getName())
                .withId(packageEntity.getId())
                .withDescription(packageEntity.getDescription())
                .withProducts(productsIds)
                .withTotalPrice(new Money(currency, totalPriceInCurrency))
                .build();
    }

    private long convertToCurrency(final Currency currency, final long totalPriceInUsd) {
        final BigDecimal exchangeRate = exchangeRatesService.getLatestRate(DEFAULT_CURRENCY, currency);

        return exchangeRate
                .multiply(new BigDecimal(totalPriceInUsd))
                .setScale(0, RoundingMode.HALF_UP)
                .longValueExact();
    }
}
