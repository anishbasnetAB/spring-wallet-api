package com.anish.wallet_api.dto;
import com.anish.wallet_api.model.WalletStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WalletResponse {
    private Long id;
    private String ownerName;
    private BigDecimal balance;
    private WalletStatus status;
    private LocalDateTime createdAt;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Long getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public WalletStatus getStatus() {
        return status;
    }

    public WalletResponse() {
    }
    public WalletResponse(
            Long id,
            String ownerName,
            BigDecimal balance,
            WalletStatus status,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.ownerName = ownerName;
        this.balance = balance;
        this.status = status;
        this.createdAt = createdAt;
    }
}
