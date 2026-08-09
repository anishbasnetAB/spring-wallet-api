package com.anish.wallet_api.service;

import com.anish.wallet_api.dto.CreateWalletRequest;
import com.anish.wallet_api.dto.MoneyOperationRequest;
import com.anish.wallet_api.dto.WalletResponse;
import com.anish.wallet_api.exception.InvalidWalletStateException;
import com.anish.wallet_api.exception.WalletNotFoundException;
import com.anish.wallet_api.mapper.WalletMapper;
import com.anish.wallet_api.model.Wallet;
import com.anish.wallet_api.model.WalletStatus;
import com.anish.wallet_api.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    public WalletResponse getWallet(Long id)
    {
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(()->
                        new WalletNotFoundException("Wallet Not found with id: " +id));

        return walletMapper.toResponse(wallet);

    }

    public List<WalletResponse> getAllWallets(){

        List<Wallet> wallets = walletRepository.findAll();

        return wallets.stream()
                .map(walletMapper::toResponse)
                .toList();
    }

    private Wallet findWalletById(Long id) {
        return walletRepository.findById(id)
                .orElseThrow(() ->
                        new WalletNotFoundException(
                                "Wallet not found with id: " + id
                        )
                );
    }

    public WalletResponse deposit(Long id, MoneyOperationRequest request)
    {
        Wallet wallet = findWalletById(id);



        if (wallet.getStatus() != WalletStatus.ACTIVE) {
            throw new InvalidWalletStateException("Wallet must be ACTIVE");
        }

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be greater than zero"
            );
        }

        BigDecimal newBalance = wallet.getBalance().add(request.getAmount());

        wallet.setBalance(newBalance);


        Wallet savedWallet = walletRepository.save(wallet);

        return walletMapper.toResponse(savedWallet);
    }

    public WalletResponse withdraw(Long id, MoneyOperationRequest request) {
        Wallet wallet = findWalletById(id);

        if (wallet.getStatus() != WalletStatus.ACTIVE) {
            throw new InvalidWalletStateException("Wallet must be ACTIVE");
        }

        if (request.getAmount().compareTo(wallet.getBalance()) > 0) {
            throw new IllegalArgumentException(
                    "Withdrawal amount exceeds current balance"
            );
        }

        BigDecimal newBalance = wallet.getBalance().subtract(request.getAmount());

        wallet.setBalance(newBalance);


        Wallet savedWallet = walletRepository.save(wallet);

        return walletMapper.toResponse(savedWallet);
    }

    public WalletResponse closeWallet(Long id) {
        Wallet wallet = findWalletById(id);

        if (wallet.getStatus() != WalletStatus.ACTIVE) {
            throw new InvalidWalletStateException("Wallet is already Closed.");
        }

        if (wallet.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalArgumentException(
                    "Amount must be 0 before account closing."
            );
        }


        wallet.setStatus(WalletStatus.CLOSED);


        Wallet savedWallet = walletRepository.save(wallet);

        return walletMapper.toResponse(savedWallet);

    }
}
