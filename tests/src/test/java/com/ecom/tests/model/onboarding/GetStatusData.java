package com.ecom.tests.model.onboarding;

public record GetStatusData(
        String merchantID,
        String terminalID,
        String requestID
) {}
