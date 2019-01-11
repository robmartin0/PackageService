package com.agitech.packageservice.core.packages;

import java.util.Currency;
import java.util.Optional;
import java.util.Set;

/**
 * Core service for managing packages
 */
public interface PackageService {

    void createOrUpdatePackage(final Package newPackage);
    Optional<Package> retrievePackage(final long packageId, final Currency currency);
    void deletePackage(final long packageId);
    Set<Package> retrieveAllPackages(final Currency currency);
}
