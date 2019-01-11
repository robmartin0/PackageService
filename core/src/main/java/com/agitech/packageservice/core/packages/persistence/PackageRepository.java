package com.agitech.packageservice.core.packages.persistence;

import org.springframework.data.repository.CrudRepository;

/**
 * Spring data repository for Package persistence
 */
public interface PackageRepository extends CrudRepository<PackageEntity, Long> {
}
