package com.agitech.packageservice.application.wrappers;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Set;

public class UpdatePackageDTO {

    private String name;
    private String description;
    private Set<String> products;

    protected UpdatePackageDTO() {
    }

    public UpdatePackageDTO(final String name, final String description, final Set<String> products) {
        this.name = name;
        this.description = description;
        this.products = products;
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
        final UpdatePackageDTO rhs = (UpdatePackageDTO) obj;
        return new EqualsBuilder()
                .append(name, rhs.name)
                .append(description, rhs.description)
                .append(products, rhs.products)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(name)
                .append(description)
                .append(products)
                .build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("description", description)
                .append("products", products)
                .build();
    }
}
