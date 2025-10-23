package com.ecom.tests.model.onboarding;

public record UpdateMerchantData(
        String merchantID,
        String terminalID,
        String accountLogin,
        String reqMerchantID,
        String reqTerminalID,
        String updatedField
) {}
