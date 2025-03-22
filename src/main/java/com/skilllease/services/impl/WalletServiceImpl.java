package com.skilllease.services.impl;

import com.skilllease.dao.WalletRepository;
import com.skilllease.entities.Wallet;
import com.skilllease.services.WalletService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class WalletServiceImpl implements WalletService {
    @Inject
    private WalletRepository walletRepository;

    @Override
    public Wallet save(Wallet wallet) {
        return walletRepository.save(wallet);
    }
}
