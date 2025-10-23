package com.ecom.tests.model.onboarding;

public record VisaFacilitatorData(
    MerchantData base,
    String visaPaymentFacilitatorId,
    String visaIndependentSalesOrgId,
    String visaSubMerchantId,
    String merchantCode,
    String terminalId,
    String merchantName) {}
