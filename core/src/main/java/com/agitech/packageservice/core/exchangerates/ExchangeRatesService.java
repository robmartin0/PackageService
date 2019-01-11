package com.agitech.packageservice.core.exchangerates;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * Responsible for retrieving current exchange rates
 */
public interface ExchangeRatesService {

    BigDecimal getLatestRate(final Currency baseCurrency, final Currency targetCurrency) throws ExchangeRateException;
}
