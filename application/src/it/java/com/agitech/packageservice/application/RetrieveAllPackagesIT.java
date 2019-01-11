package com.agitech.packageservice.application;

import com.agitech.packageservice.application.wrappers.PackageWithPriceDTO;
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

import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RetrieveAllPackagesIT {

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
    public void packageExists() {

        packageRepository.save(
                new PackageEntity(1L,
                        "Test Package",
                        "test Description",
                        Collections.singleton("VqKb4tyj9V6i")));

        productService
                .expect(once(), requestTo("https://product-service.herokuapp.com/api/v1/products/VqKb4tyj9V6i"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withSuccess(
                                "{\"id\":\"VqKb4tyj9V6i\",\"name\":\"Shield\",\"usdPrice\":1149}",
                                MediaType.APPLICATION_JSON));

        PackageWithPriceDTO expectedPackage = new PackageWithPriceDTO(
                1,
                "Test Package",
                "test Description",
                Collections.singleton("VqKb4tyj9V6i"),
                "USD",
                1149);

        webClient.get().uri("/v1/package/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PackageWithPriceDTO.class).isEqualTo(expectedPackage);
    }
}
