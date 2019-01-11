package com.agitech.packageservice.application.wrappers;

import com.agitech.packageservice.core.packages.Package;
import com.agitech.packageservice.core.utils.Money;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Set;

public class PackageWithPriceDTO {

    private long id;
    private String name;
    private String description;
    private Set<String> products;
    private String currency;
    private long totalPriceInCurrency;

    protected PackageWithPriceDTO() {
    }

    public PackageWithPriceDTO(final long id,
                               final String name,
                               final String description,
                               final Set<String> products,
                               final String currency,
                               final long totalPriceInCurrency) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.products = products;
        this.currency = currency;
        this.totalPriceInCurrency = totalPriceInCurrency;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<String> getProducts() {
        return products;
    }

    public String getCurrency() {
        return currency;
    }

    public long getTotalPriceInCurrency() {
        return totalPriceInCurrency;
    }

    public static PackageWithPriceDTO toPackageDTO(final Package aPackage) {

        final Money totalPrice = aPackage.getTotalPrice().orElseThrow(() -> new RuntimeException("Could not retrieve prices"));

        final long id = aPackage.getId().orElseThrow(() -> new RuntimeException("Packages should have an ID"));

        return new PackageWithPriceDTO(
                id,
                aPackage.getName(),
                aPackage.getDescription().orElse(null),
                aPackage.getProducts(),
                totalPrice.getCurrency().getCurrencyCode(),
                totalPrice.getAmount());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        final PackageWithPriceDTO rhs = (PackageWithPriceDTO) obj;
        return new EqualsBuilder()
                .append(id, rhs.id)
                .append(name, rhs.name)
                .append(description, rhs.description)
                .append(products, rhs.products)
                .append(currency, rhs.currency)
                .append(totalPriceInCurrency, rhs.totalPriceInCurrency)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(name)
                .append(description)
                .append(products)
                .append(currency)
                .append(totalPriceInCurrency)
                .build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("description", description)
                .append("products", products)
                .append("currency", currency)
                .append("totalPriceInCurrency", totalPriceInCurrency)
                .build();
    }
}
