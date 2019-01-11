package com.agitech.packageservice.core.exchangerates;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

class ExchangeRates {

    private boolean success;
    private Map<String, BigDecimal> rates;
    private Error error;

    protected ExchangeRates() {
    }

    private ExchangeRates(final boolean success, final Map<String, BigDecimal> rates, final Error error) {
        this.success = success;
        this.rates = rates;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    Optional<Error> getError() {
        return Optional.ofNullable(error);
    }

    Optional<BigDecimal> getRate(final String currency) {
        if (rates == null || !rates.containsKey(currency)) {
            return Optional.empty();
        } else {
            return Optional.of(rates.get(currency));
        }
    }

    static ExchangeRates success(final Map<String, BigDecimal> rates) {
        return new ExchangeRates(true, rates, null);
    }

    static ExchangeRates failure(final Error error) {
        return new ExchangeRates(false, Collections.emptyMap(), error);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("success", success)
                .append("rates", rates)
                .append("error", error)
                .build();
    }

    static class Error {

        private int code;
        private String type;
        private String description;

        protected Error() {
        }

        Error(final int code, final String type, final String description) {
            this.code = code;
            this.type = type;
            this.description = description;
        }

        public int getCode() {
            return code;
        }

        public String getType() {
            return type;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("code", code)
                    .append("type", type)
                    .append("description", description)
                    .build();
        }
    }
}
