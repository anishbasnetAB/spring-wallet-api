package com.anish.wallet_api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.math.BigDecimal;

public class CreateWalletRequest {

    @NotBlank
    private String ownerName;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal openingBalance;

    public String getOwnerName() {
        return ownerName;
    }

    public BigDecimal getOpeningBalance() {
        return openingBalance;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setOpeningBalance(BigDecimal openingBalance) {
        this.openingBalance = openingBalance;
    }

    public CreateWalletRequest() {
    }
}
