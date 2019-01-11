package com.agitech.packageservice.core.exchangerates;

import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Currency;

public class ExchangeRatesServiceRest implements ExchangeRatesService {

    private static final String EXCHANGE_RATE_SERVICE_ENDPOINT = "/latest?access_key={access_key}&symbols={symbols}";

    private final RestTemplate restTemplate;
    private final String apiKey;

    public ExchangeRatesServiceRest(final RestTemplate restTemplate, final String apiKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    @Override
    public BigDecimal getLatestRate(final Currency baseCurrency, final Currency targetCurrency)
            throws ExchangeRateException {

        final String currenciesToRetrieve = baseCurrency + "," + targetCurrency;

        final ExchangeRates exchangeRates = restTemplate.getForObject(
                EXCHANGE_RATE_SERVICE_ENDPOINT,
                ExchangeRates.class,
                apiKey, currenciesToRetrieve);

        if (exchangeRates == null) {
            throw new ExchangeRateException("Exchange rates empty");
        }

        if (!exchangeRates.isSuccess()) {
            final String errorMessage = exchangeRates.getError()
                    .map(ExchangeRates.Error::toString)
                    .orElse("Could not retrieve exchange rates");

            throw new ExchangeRateException(errorMessage);
        }

        final BigDecimal baseRate = exchangeRates.getRate(baseCurrency.getCurrencyCode())
                .orElseThrow(() -> new ExchangeRateException("Could not retrieve rate for " + baseCurrency));

        final BigDecimal targetRate = exchangeRates.getRate(targetCurrency.getCurrencyCode())
                .orElseThrow(() -> new ExchangeRateException("Could not retrieve rate for " + targetCurrency));

        return targetRate.divide(baseRate, 6, BigDecimal.ROUND_HALF_UP);
    }
}
