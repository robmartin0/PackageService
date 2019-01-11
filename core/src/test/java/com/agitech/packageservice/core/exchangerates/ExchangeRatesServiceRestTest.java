package com.agitech.packageservice.core.exchangerates;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ExchangeRatesServiceRestTest {

    private final static String EXPECTED_URL = "/latest?access_key={access_key}&symbols={symbols}";
    private final static Currency USD = Currency.getInstance("USD");
    private final static Currency GBP = Currency.getInstance("GBP");

    @Mock
    private RestTemplate restTemplate;

    private ExchangeRatesServiceRest exchangeRatesServiceRest;

    @Before
    public void setUp() {
        exchangeRatesServiceRest = new ExchangeRatesServiceRest(restTemplate, "XXX");
    }

    @Test
    public void getLatestRateWillCalculateRate() throws ExchangeRateException {

        // Given
        final Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("USD", new BigDecimal("1.153562"));
        rates.put("GBP", new BigDecimal("0.90411"));

        given(restTemplate.getForObject(EXPECTED_URL, ExchangeRates.class, "XXX", "USD,GBP"))
                .willReturn(ExchangeRates.success(rates));

        // When
        final BigDecimal latestRate = exchangeRatesServiceRest.getLatestRate(USD, GBP);

        // Then
        assertThat(latestRate, is(new BigDecimal("0.783755")));
    }

    @Test(expected = ExchangeRateException.class)
    public void getLatestRateWillThrowExceptionWhenUnsuccessful() throws ExchangeRateException {

        // Given
        given(restTemplate.getForObject(EXPECTED_URL, ExchangeRates.class, "XXX", "USD,GBP"))
                .willReturn(ExchangeRates.failure(
                        new ExchangeRates.Error(
                                101,
                                "invalid_access_key",
                                "You have not supplied a valid API Access Key.")));

        // When
        exchangeRatesServiceRest.getLatestRate(Currency.getInstance("USD"), Currency.getInstance("GBP"));
    }

    @Test(expected = ExchangeRateException.class)
    public void getLatestRateWillThrowExceptionWhenMissingBaseRate() throws ExchangeRateException {

        // Given
        final Map<String, BigDecimal> rates = Collections.singletonMap("GBP", new BigDecimal("0.90411"));

        given(restTemplate.getForObject(EXPECTED_URL, ExchangeRates.class, "XXX", "USD,GBP"))
                .willReturn(ExchangeRates.success(rates));

        // When
        exchangeRatesServiceRest.getLatestRate(Currency.getInstance("USD"), Currency.getInstance("GBP"));
    }

    @Test(expected = ExchangeRateException.class)
    public void getLatestRateWillThrowExceptionWhenMissingTargetRate() throws ExchangeRateException {

        // Given
        final Map<String, BigDecimal> rates = Collections.singletonMap("USD", new BigDecimal("1.153562"));

        given(restTemplate.getForObject(EXPECTED_URL, ExchangeRates.class, "XXX", "USD,GBP"))
                .willReturn(ExchangeRates.success(rates));

        // When
        exchangeRatesServiceRest.getLatestRate(Currency.getInstance("USD"), Currency.getInstance("GBP"));
    }
}