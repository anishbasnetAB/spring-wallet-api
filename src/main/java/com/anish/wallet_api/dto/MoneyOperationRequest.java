package com.anish.wallet_api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class MoneyOperationRequest {

    @NotNull(message = "Amount is required")
    @DecimalMin(
            value = "0.01",
            inclusive = true,
            message = "Amount must be greater than zero"
    )
    private BigDecimal amount;

    public MoneyOperationRequest() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}