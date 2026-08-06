package com.anish.wallet_api.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;


@Entity
@Table(name ="wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wallet_seq")
    @SequenceGenerator(
            name="wallet_seq",
            sequenceName = "wallet_sequence",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WalletStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public WalletStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setStatus(WalletStatus status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    protected Wallet(){

    }

    public Wallet(
            String ownerName,
            BigDecimal balance,
            WalletStatus status,
            LocalDateTime createdAt
    ){
        this.ownerName=ownerName;
        this.balance=balance;
        this.status=status;
        this.createdAt=createdAt;
    }
}
