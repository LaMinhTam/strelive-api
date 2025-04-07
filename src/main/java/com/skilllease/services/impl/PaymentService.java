package com.skilllease.services.impl;

import com.skilllease.configurations.VNPAYConfig;
import com.skilllease.dao.WalletRepository;
import com.skilllease.dto.PaymentResponse;
import com.skilllease.entities.*;
import com.skilllease.exception.AppException;
import com.skilllease.exception.ErrorCode;
import com.skilllease.services.ContractService;
import com.skilllease.services.MilestoneService;
import com.skilllease.services.TransactionService;
import com.skilllease.utils.AuthService;
import com.skilllease.utils.VNPayUtil;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.Map;

@Stateless
public class PaymentService {
    @Inject
    private WalletRepository walletRepository;
    @Inject
    private TransactionService transactionService;
    @Inject
    private VNPAYConfig vnpayConfig;
    @Inject
    private AuthService authService;
    @Inject
    private ContractService contractService;
    @Inject
    private MilestoneService milestoneService;

    public PaymentResponse createVnPayPayment(HttpServletRequest request) throws AppException {
        // Get the amount parameter and convert it (multiply by 100)
        long amount = Integer.parseInt(request.getParameter("amount")) * 100L;
        String bankCode = request.getParameter("bankCode");

        // Get the default VNPay parameters from the configuration
        Map<String, String> vnpParamsMap = vnpayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));

        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnpayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;

        String paymentUrl = vnpayConfig.getVnpPayUrl() + "?" + queryUrl;

        User user = authService.getCurrentUser();
        Wallet wallet = walletRepository.findByUserId(user.getId()).orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));
        Transaction transaction = Transaction.builder()
                .amount(new BigDecimal(amount / 100))
                .balance(wallet.getBalance())
                .reference(vnpParamsMap.get("vnp_TxnRef"))
                .type(TransactionType.DEPOSIT)
                .wallet(wallet)
                .build();
        transactionService.saveTransaction(transaction);
        return new PaymentResponse("ok", "Success", paymentUrl);
    }

    public void handleVnPayCallback(HttpServletRequest request) throws AppException {
        Map<String, String[]> vnpParams = request.getParameterMap();
        if (vnpParams.containsKey("vnp_SecureHash")) {
            String vnpSecureHash = request.getParameter("vnp_SecureHash");
            vnpParams.remove("vnp_SecureHash");
            String hashData = VNPayUtil.hashAllFields(vnpParams);
            String secretKey = vnpayConfig.getSecretKey();
            String checkSum = VNPayUtil.hmacSHA512(secretKey, hashData);
            if (!vnpSecureHash.equals(checkSum)) {
                throw new AppException(ErrorCode.INVALID_HASH_DATA);
            }
        }

        String status = request.getParameter("vnp_ResponseCode");
        if (!"00".equals(status)) {
            throw new AppException(ErrorCode.ATTEMPT_ERROR);
        }
        String reference = request.getParameter("vnp_TxnRef");
        Transaction transaction = transactionService.findByReference(reference).orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));
        Wallet wallet = transaction.getWallet();
        wallet.setBalance(wallet.getBalance().add(transaction.getAmount()));
        walletRepository.save(wallet);
    }

    public void transferFinalPayment(Milestone milestone) throws AppException {
        Contract contract = milestone.getContract();
        User user = contract.getFreelancer();
        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        Transaction transaction = Transaction.builder()
                .amount(contract.getFinalPaymentAmount())
                .balance(wallet.getBalance())
                .type(TransactionType.PAYMENT)
                .wallet(wallet)
                .build();
        transactionService.saveTransaction(transaction);
        wallet.setBalance(wallet.getBalance().add(contract.getFinalPaymentAmount()));
        walletRepository.save(wallet);
    }

    public boolean isWalletBalanceEnough(Long id, BigDecimal price) throws AppException {
        Wallet wallet = walletRepository.findByUserId(id)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));
        return wallet.getBalance().compareTo(price) >= 0;
    }

    public PaymentResponse payForContract(Long id) throws AppException {
        Contract contract = contractService.getContractById(id);
        User user = authService.getCurrentUser();
        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));
        if (wallet.getBalance().compareTo(contract.getDepositAmount()) < 0) {
            throw new AppException(ErrorCode.INSUFFICIENT_BALANCE);
        }
        Transaction transaction = Transaction.builder()
                .amount(contract.getDepositAmount())
                .balance(wallet.getBalance())
                .reference(contract.getId().toString())
                .type(TransactionType.DEPOSIT)
                .wallet(wallet)
                .build();
        transactionService.saveTransaction(transaction);
        wallet.setBalance(wallet.getBalance().subtract(contract.getDepositAmount()));
        walletRepository.save(wallet);

        return new PaymentResponse("ok", "Success", null);
    }

    public PaymentResponse payForMilestone(Long id) throws AppException {
        Milestone milestone = milestoneService.getMilestoneById(id);
        User user = authService.getCurrentUser();
        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));
        if (wallet.getBalance().compareTo(milestone.getContract().getFinalPaymentAmount()) < 0) {
            throw new AppException(ErrorCode.INSUFFICIENT_BALANCE);
        }
        Transaction transaction = Transaction.builder()
                .amount(milestone.getContract().getFinalPaymentAmount())
                .balance(wallet.getBalance())
                .reference(milestone.getId().toString())
                .type(TransactionType.DEPOSIT)
                .wallet(wallet)
                .build();
        transactionService.saveTransaction(transaction);
        wallet.setBalance(wallet.getBalance().subtract(milestone.getContract().getFinalPaymentAmount()));
        walletRepository.save(wallet);
        milestone.setHidden(false);
        milestoneService.updateMilestone(milestone);
        return new PaymentResponse("ok", "Success", null);
    }

    public Wallet getUserWallet() throws AppException {
        User user = authService.getCurrentUser();
        return walletRepository.findByUserId(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));
    }
}
