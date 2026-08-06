package com.anish.wallet_api.dto;

import java.math.BigDecimal;

public class MoneyOperationRequest {
    private BigDecimal amount;

    public MoneyOperationRequest(){

    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
