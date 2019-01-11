package com.agitech.packageservice.application.controllers;


import com.agitech.packageservice.application.wrappers.PackageWithPriceDTO;
import com.agitech.packageservice.application.wrappers.UpdatePackageDTO;
import com.agitech.packageservice.core.packages.Package;
import com.agitech.packageservice.core.packages.PackageService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/package")
public class PackageController {

    private final PackageService packageService;

    public PackageController(final PackageService packageService) {
        this.packageService = packageService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    void createPackage(@RequestBody final UpdatePackageDTO packageDTO) {

        final Package newPackage = new Package.Builder(packageDTO.getName())
                .withDescription(packageDTO.getDescription())
                .withProducts(packageDTO.getProducts())
                .build();

        packageService.createOrUpdatePackage(newPackage);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{packageId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void updatePackage(@RequestBody final UpdatePackageDTO packageDTO,
                       @PathVariable final long packageId) {

        final Package updatedPackage = new Package.Builder(packageDTO.getName())
                .withId(packageId)
                .withDescription(packageDTO.getDescription())
                .withProducts(packageDTO.getProducts())
                .build();

        packageService.createOrUpdatePackage(updatedPackage);
    }

    @RequestMapping(method = RequestMethod.GET)
    Set<PackageWithPriceDTO> retrieveAllPackages(@RequestParam(defaultValue = "USD") final String currencyCode) {
        final Currency currency = Currency.getInstance(currencyCode);
        return packageService.retrieveAllPackages(currency).stream()
                .map(PackageWithPriceDTO::toPackageDTO)
                .collect(Collectors.toSet());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{packageId}")
    PackageWithPriceDTO retreivePackage(@PathVariable final long packageId,
                                        @RequestParam(defaultValue = "USD") final String currencyCode) {

        final Currency currency = Currency.getInstance(currencyCode);
        final Package aPackage = packageService.retrievePackage(packageId, currency)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Package not found"));

        return PackageWithPriceDTO.toPackageDTO(aPackage);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{packageId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void deletePackage(@PathVariable final long packageId) {
        packageService.deletePackage(packageId);
    }

}
