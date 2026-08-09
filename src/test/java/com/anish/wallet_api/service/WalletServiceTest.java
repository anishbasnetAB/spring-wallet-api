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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private WalletMapper walletMapper;

    @InjectMocks
    private WalletService walletService;

    private Wallet wallet;

    @BeforeEach
    void setUp() {
        wallet = new Wallet(
                "Anish",
                new BigDecimal("100.00"),
                WalletStatus.ACTIVE,
                LocalDateTime.now()
        );
    }

    @Test
    void getWallet_shouldReturnWallet_whenWalletExists() {

        when(walletRepository.findById(1L))
                .thenReturn(Optional.of(wallet));

        WalletResponse expectedResponse = new WalletResponse();

        when(walletMapper.toResponse(wallet))
                .thenReturn(expectedResponse);

        WalletResponse result = walletService.getWallet(1L);

        assertEquals(expectedResponse, result);

        verify(walletRepository).findById(1L);
        verify(walletMapper).toResponse(wallet);
    }

    @Test
    void getWallet_shouldThrowException_whenWalletDoesNotExist() {

        when(walletRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                WalletNotFoundException.class,
                () -> walletService.getWallet(99L)
        );

        verify(walletRepository).findById(99L);
        verifyNoInteractions(walletMapper);
    }

    @Test
    void deposit_shouldIncreaseBalance() {

        MoneyOperationRequest request = new MoneyOperationRequest();
        request.setAmount(new BigDecimal("50.00"));

        when(walletRepository.findById(1L))
                .thenReturn(Optional.of(wallet));

        when(walletRepository.save(any(Wallet.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        WalletResponse response = new WalletResponse();

        when(walletMapper.toResponse(wallet))
                .thenReturn(response);

        walletService.deposit(1L, request);

        assertEquals(
                0,
                wallet.getBalance().compareTo(new BigDecimal("150.00"))
        );

        verify(walletRepository).save(wallet);
    }

    @Test
    void deposit_shouldThrowException_whenWalletIsClosed() {

        Wallet closedWallet = new Wallet(
                "Anish",
                BigDecimal.ZERO,
                WalletStatus.CLOSED,
                LocalDateTime.now()
        );

        MoneyOperationRequest request = new MoneyOperationRequest();
        request.setAmount(new BigDecimal("50.00"));

        when(walletRepository.findById(1L))
                .thenReturn(Optional.of(closedWallet));

        assertThrows(
                InvalidWalletStateException.class,
                () -> walletService.deposit(1L, request)
        );

        verify(walletRepository, never()).save(any());
    }

    @Test
    void withdraw_shouldDecreaseBalance() {

        MoneyOperationRequest request = new MoneyOperationRequest();
        request.setAmount(new BigDecimal("40.00"));

        when(walletRepository.findById(1L))
                .thenReturn(Optional.of(wallet));

        when(walletRepository.save(any(Wallet.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        WalletResponse response = new WalletResponse();

        when(walletMapper.toResponse(wallet))
                .thenReturn(response);

        walletService.withdraw(1L, request);

        assertEquals(
                0,
                wallet.getBalance().compareTo(new BigDecimal("60.00"))
        );

        verify(walletRepository).save(wallet);
    }

    @Test
    void withdraw_shouldThrowException_whenAmountExceedsBalance() {

        MoneyOperationRequest request = new MoneyOperationRequest();
        request.setAmount(new BigDecimal("200.00"));

        when(walletRepository.findById(1L))
                .thenReturn(Optional.of(wallet));

        assertThrows(
                IllegalArgumentException.class,
                () -> walletService.withdraw(1L, request)
        );

        verify(walletRepository, never()).save(any());
    }

    @Test
    void closeWallet_shouldThrowException_whenBalanceIsNotZero() {

        when(walletRepository.findById(1L))
                .thenReturn(Optional.of(wallet));

        assertThrows(
                InvalidWalletStateException.class,
                () -> walletService.closeWallet(1L)
        );

        verify(walletRepository, never()).save(any());
    }

    @Test
    void closeWallet_shouldCloseWallet_whenBalanceIsZero() {

        Wallet zeroBalanceWallet = new Wallet(
                "Anish",
                BigDecimal.ZERO,
                WalletStatus.ACTIVE,
                LocalDateTime.now()
        );

        when(walletRepository.findById(1L))
                .thenReturn(Optional.of(zeroBalanceWallet));

        when(walletRepository.save(any(Wallet.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        WalletResponse response = new WalletResponse();

        when(walletMapper.toResponse(zeroBalanceWallet))
                .thenReturn(response);

        walletService.closeWallet(1L);

        assertEquals(
                WalletStatus.CLOSED,
                zeroBalanceWallet.getStatus()
        );

        verify(walletRepository).save(zeroBalanceWallet);
    }
}