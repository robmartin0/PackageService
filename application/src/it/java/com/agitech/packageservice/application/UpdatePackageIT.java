package com.agitech.packageservice.application;

import com.agitech.packageservice.application.wrappers.UpdatePackageDTO;
import com.agitech.packageservice.core.packages.persistence.PackageEntity;
import com.agitech.packageservice.core.packages.persistence.PackageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UpdatePackageIT {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private RestTemplate productServiceRestTemplate;

    private MockRestServiceServer productService;

    @Before
    public void setUp() {
        productService = MockRestServiceServer.bindTo(productServiceRestTemplate).build();
    }

    @Test
    public void updatePackage() {

        packageRepository.save(
                new PackageEntity(1L,
                        "Test Package",
                        "test Description",
                        Collections.singleton("VqKb4tyj9V6i")));

        final UpdatePackageDTO updatedPackage = new UpdatePackageDTO(
                "Test Package 2",
                "test description 2",
                Collections.singleton("PKM5pGAh9yGm"));

        productService
                .expect(once(), requestTo("https://product-service.herokuapp.com/api/v1/products/PKM5pGAh9yGm"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withSuccess(
                                "{\"id\":\"PKM5pGAh9yGm\",\"name\":\"Axe\",\"usdPrice\":799}",
                                MediaType.APPLICATION_JSON));

        webClient.put().uri("v1/package/1")
                .syncBody(updatedPackage)
                .exchange()
                .expectStatus().isAccepted();

        final Optional<PackageEntity> packageEntity = packageRepository.findById(1L);
        assertThat(packageEntity.isPresent(), is(true));
        assertThat(packageEntity.get().getId(), is(1L));
        assertThat(packageEntity.get().getName(), is("Test Package 2"));
        assertThat(packageEntity.get().getDescription(), is("test description 2"));
        assertThat(packageEntity.get().getProducts(), contains("PKM5pGAh9yGm"));
    }
}
