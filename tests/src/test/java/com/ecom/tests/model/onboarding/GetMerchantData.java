package com.ecom.tests.model.onboarding;

public record GetMerchantData(
        String merchantID,
        String terminalID,
        String accountLogin,
        String reqMerchantID,
        String reqTerminalID
) {}

