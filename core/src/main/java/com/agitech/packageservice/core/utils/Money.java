package com.agitech.packageservice.core.utils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Currency;

/**
 * Value object to represent money. Note when JSR-354 is supported by Java this class is redundant.
 */
public class Money {

    private final Currency currency;
    private final long amount;

    public Money(final Currency currency, final long amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public long getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        final Money rhs = (Money) obj;
        return new EqualsBuilder()
                .append(currency, rhs.currency)
                .append(amount, rhs.amount)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(currency)
                .append(amount)
                .build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("currency", currency)
                .append("amount", amount)
                .build();
    }
}
