package com.anish.wallet_api.mapper;
import com.anish.wallet_api.dto.WalletResponse;
import com.anish.wallet_api.model.Wallet;

public class WalletMapper {
    public WalletResponse toResponse(Wallet wallet) {
        return new WalletResponse(
                wallet.getId(),
                wallet.getOwnerName(),
                wallet.getBalance(),
                wallet.getStatus(),
                wallet.getCreatedAt()
        );
    }
}
