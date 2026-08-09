package com.anish.wallet_api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.math.BigDecimal;

public class CreateWalletRequest {

    @NotBlank(message = "Owner name is required")
    private String ownerName;

    @NotNull(message = "Opening balance is required")
    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "Opening balance cannot be negative"
    )
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

