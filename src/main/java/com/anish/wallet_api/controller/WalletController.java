package com.anish.wallet_api.controller;

import com.anish.wallet_api.dto.MoneyOperationRequest;
import com.anish.wallet_api.service.WalletService;
import org.springframework.web.bind.annotation.*;
import com.anish.wallet_api.dto.CreateWalletRequest;
import com.anish.wallet_api.dto.WalletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<WalletResponse> createWallet(
            @Valid @RequestBody CreateWalletRequest request
    ) {
        WalletResponse response = walletService.createWallet(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletResponse> getWallet(
            @PathVariable Long id
    ) {
        WalletResponse response = walletService.getWallet(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<WalletResponse>> getAllWallets() {
        List<WalletResponse> response = walletService.getAllWallets();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/deposits")
    public ResponseEntity<WalletResponse> deposit(
            @PathVariable Long id,
            @Valid @RequestBody MoneyOperationRequest request
    ) {
        WalletResponse response = walletService.deposit(id, request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/withdrawals")
    public ResponseEntity<WalletResponse> withdraw(
            @PathVariable Long id,
            @Valid @RequestBody MoneyOperationRequest request
    ) {
        WalletResponse response = walletService.withdraw(id, request);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<WalletResponse> closeWallet(
            @PathVariable Long id
    ) {
        WalletResponse response = walletService.closeWallet(id);

        return ResponseEntity.ok(response);
    }
}