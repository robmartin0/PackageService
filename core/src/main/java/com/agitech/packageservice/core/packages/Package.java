package com.agitech.packageservice.core.packages;

import com.agitech.packageservice.core.utils.Money;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collections;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.Set;

public class Package {

    private final Long id;
    private final String name;
    private final String description;
    private final Set<String> products;
    private final Money totalPrice;

    private Package(final Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.products = builder.products;
        this.totalPrice = builder.totalPrice;
    }

    public OptionalLong getId() {
        return (id == null) ? OptionalLong.empty() : OptionalLong.of(id);
    }

    public String getName() {
        return name;
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public Set<String> getProducts() {
        return products;
    }

    public Optional<Money> getTotalPrice() {
        return Optional.ofNullable(totalPrice);
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
        final Package rhs = (Package) obj;
        return new EqualsBuilder()
                .append(id, rhs.id)
                .append(name, rhs.name)
                .append(description, rhs.description)
                .append(products, rhs.products)
                .append(totalPrice, rhs.totalPrice)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(name)
                .append(description)
                .append(products)
                .append(totalPrice)
                .build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("description", description)
                .append("products", products)
                .append("totalPrice", totalPrice)
                .build();
    }

    public static class Builder {

        private Long id;
        private final String name;
        private String description;
        private Set<String> products;
        private Money totalPrice;

        public Builder(String name) {
            this.name = name;
        }

        public Builder withId(final long id) {
            this.id = id;
            return this;
        }

        public Builder withDescription(final String description) {
            this.description = description;
            return this;
        }

        public Builder withProducts(final Set<String> products) {
            this.products = products;
            return this;
        }

        public Builder withTotalPrice(final Money totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public Package build() {
            if (products == null) {
                products = Collections.emptySet();
            }
            return new Package(this);
        }
    }
}
