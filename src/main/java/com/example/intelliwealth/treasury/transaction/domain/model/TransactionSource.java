package com.example.intelliwealth.core.transaction.domain.model;

public enum TransactionSource {

    // Banking Channels
    BANK_TRANSFER,
    UPI,
    DEBIT_CARD,
    CREDIT_CARD,

    // Digital Wallets
    WALLET,
    NET_BANKING,

    // Cash
    CASH,

    // Investments
    BROKERAGE,
    MUTUAL_FUND,
    STOCK_MARKET,

    // Income Sources
    EMPLOYER,
    BUSINESS,
    FREELANCE_CLIENT,

    // System / Internal
    AUTO_DEBIT,
    REFUND,
    ADJUSTMENT,

    // Misc
    OTHER
}
