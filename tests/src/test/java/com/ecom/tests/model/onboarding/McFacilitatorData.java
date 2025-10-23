package com.ecom.tests.model.onboarding;

public record McFacilitatorData(
        MerchantData base,
        String mcPaymentFacilitatorId,
        String mcIndependentSalesOrgId,
        String mcSubMerchantId
) {}
