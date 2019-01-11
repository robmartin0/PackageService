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
public class CreatePackageIT {

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
    public void createPackage() {

        productService
                .expect(once(), requestTo("https://product-service.herokuapp.com/api/v1/products/VqKb4tyj9V6i"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withSuccess(
                                "{\"id\":\"VqKb4tyj9V6i\",\"name\":\"Shield\",\"usdPrice\":1149}",
                                MediaType.APPLICATION_JSON));

        final UpdatePackageDTO newPackage = new UpdatePackageDTO(
                "Test Package",
                "test description",
                Collections.singleton("VqKb4tyj9V6i"));

        webClient.post().uri("v1//package")
                .syncBody(newPackage)
                .exchange()
                .expectStatus().isCreated();

        final Optional<PackageEntity> packageEntity = packageRepository.findById(1L);
        assertThat(packageEntity.isPresent(), is(true));
        assertThat(packageEntity.get().getId(), is(1L));
        assertThat(packageEntity.get().getName(), is("Test Package"));
        assertThat(packageEntity.get().getDescription(), is("test description"));
        assertThat(packageEntity.get().getProducts(), contains("VqKb4tyj9V6i"));
    }
}
