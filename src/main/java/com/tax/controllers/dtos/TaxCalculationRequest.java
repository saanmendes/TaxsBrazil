package com.tax.controllers.dtos;

public class TaxCalculationRequest {
    private Long taxTypeId;
    private double baseValue;

    public TaxCalculationRequest(Long taxTypeId, double baseValue) {
        this.taxTypeId = taxTypeId;
        this.baseValue = baseValue;
    }

    public TaxCalculationRequest() {
    }

    public Long getTaxTypeId() {
        return taxTypeId;
    }

    public void setTaxTypeId(Long taxTypeId) {
        this.taxTypeId = taxTypeId;
    }

    public double getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(double baseValue) {
        this.baseValue = baseValue;
    }
}
