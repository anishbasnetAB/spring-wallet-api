package com.anish.wallet_api.repository;
import com.anish.wallet_api.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet,Long> {
}
