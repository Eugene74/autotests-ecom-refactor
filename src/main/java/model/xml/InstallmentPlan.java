package model.xml;

public class InstallmentPlan {
    private String instPlanParamId;
    private String numberOfPay;
    private String feeMonth;
    private String interestRate;
    private String subsequentAmount;
    private String subsequentFee;
    private String checkValue;

    public String getInstPlanParamId() {
        return instPlanParamId;
    }

    public void setInstPlanParamId(String instPlanParamId) {
        this.instPlanParamId = instPlanParamId;
    }

    public String getNumberOfPay() {
        return numberOfPay;
    }

    public void setNumberOfPay(String numberOfPay) {
        this.numberOfPay = numberOfPay;
    }

    public String getFeeMonth() {
        return feeMonth;
    }

    public void setFeeMonth(String feeMonth) {
        this.feeMonth = feeMonth;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getSubsequentAmount() {
        return subsequentAmount;
    }

    public void setSubsequentAmount(String subsequentAmount) {
        this.subsequentAmount = subsequentAmount;
    }

    public String getSubsequentFee() {
        return subsequentFee;
    }

    public void setSubsequentFee(String subsequentFee) {
        this.subsequentFee = subsequentFee;
    }

    public String getCheckValue() {
        return checkValue;
    }

    public void setCheckValue(String checkValue) {
        this.checkValue = checkValue;
    }
}
