package com.ecom.tests.model.onboarding;

public record DeleteMerchantData(
    String merchantID,
    String terminalID,
    String accountLogin,
    String reqMerchantID,
    String reqTerminalID) {}
