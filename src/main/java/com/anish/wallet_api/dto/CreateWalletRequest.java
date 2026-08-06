package com.anish.wallet_api.dto;

import java.math.BigDecimal;

public class CreateWalletRequest {
    private String ownerName;
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
