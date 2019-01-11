package com.agitech.packageservice.application;

import com.agitech.packageservice.core.packages.persistence.PackageEntity;
import com.agitech.packageservice.core.packages.persistence.PackageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeletePackageIT {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private PackageRepository packageRepository;

    @Test
    public void deletePackage() {

        packageRepository.save(
                new PackageEntity(1L,
                        "Test Package",
                        "test Description",
                        Collections.singleton("VqKb4tyj9V6i")));

        webClient.get().uri("/v1/package/1")
                .exchange()
                .expectStatus().isOk();

        webClient.delete().uri("/v1/package/1")
                .exchange()
                .expectStatus().isOk();

        webClient.get().uri("/v1/package/1")
                .exchange()
                .expectStatus().isNotFound();
    }
}
