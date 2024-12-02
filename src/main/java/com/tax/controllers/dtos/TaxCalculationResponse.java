package com.tax.controllers.dtos;

public class TaxCalculationResponse {
    private String taxType;
    private double baseValue;
    private double aliquot;
    private double taxAmount;



    public TaxCalculationResponse(String taxType, double baseValue, double aliquot, double taxAmount) {
        this.taxType = taxType;
        this.baseValue = baseValue;
        this.aliquot = aliquot;
        this.taxAmount = taxAmount;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public double getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(double baseValue) {
        this.baseValue = baseValue;
    }

    public double getAliquot() {
        return aliquot;
    }

    public void setAliquot(double aliquot) {
        this.aliquot = aliquot;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }
}
