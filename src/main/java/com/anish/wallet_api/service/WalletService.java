package com.anish.wallet_api.service;

import com.anish.wallet_api.dto.CreateWalletRequest;
import com.anish.wallet_api.dto.WalletResponse;
import com.anish.wallet_api.mapper.WalletMapper;
import com.anish.wallet_api.model.Wallet;
import com.anish.wallet_api.model.WalletStatus;
import com.anish.wallet_api.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;

    public WalletService(
            WalletRepository walletRepository,
            WalletMapper walletMapper
    ) {
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
    }

    public WalletResponse createWallet(CreateWalletRequest request) {

        Wallet wallet = new Wallet(
                request.getOwnerName(),
                request.getOpeningBalance(),
                WalletStatus.ACTIVE,
                LocalDateTime.now()
        );

        Wallet savedWallet = walletRepository.save(wallet);

        return walletMapper.toResponse(savedWallet);
    }
}
