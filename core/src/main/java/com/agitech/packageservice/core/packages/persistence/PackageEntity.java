package com.agitech.packageservice.core.packages.persistence;

import com.agitech.packageservice.core.packages.Package;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
public class PackageEntity implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column()
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "package_products", joinColumns = @JoinColumn(name = "product_id"))
    private Set<String> products;

    protected PackageEntity() {
    }

    public PackageEntity(Long id, String name, String description, Set<String> products) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.products = products;
    }

    public static PackageEntity fromPackage(final Package newPackage) {
        if (newPackage.getId().isPresent()) {
            return new PackageEntity(
                    newPackage.getId().getAsLong(),
                    newPackage.getName(),
                    newPackage.getDescription().orElse(null),
                    newPackage.getProducts()
            );
        } else {
            return new PackageEntity(
                    null,
                    newPackage.getName(),
                    newPackage.getDescription().orElse(null),
                    newPackage.getProducts()
            );
        }
    }

    public Long getId() {
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
}
