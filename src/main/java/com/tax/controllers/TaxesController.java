package com.tax.controllers;

import com.tax.controllers.dtos.TaxCalculationResponse;
import com.tax.models.TaxCalculation;
import com.tax.controllers.dtos.TaxCalculationRequest;
import com.tax.models.TaxType;
import com.tax.repositories.TaxRepository;
import com.tax.services.TaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taxes")
public class TaxesController {

    @Autowired
    private TaxService taxService;

    @GetMapping("/types")
    public List<TaxType> getAllTaxTypes() {
        return taxService.getAllTaxTypes();
    }

    @PostMapping("/types")
    public TaxType createTaxType(@RequestBody TaxType taxType) {
        return taxService.createTaxType(taxType);
    }

    @PostMapping("/calculation")
    public ResponseEntity<TaxCalculationResponse> calculateTax(@RequestBody TaxCalculationRequest request) {
        TaxCalculation calculation = taxService.calculateTax(request);
        if (calculation == null) {
            return ResponseEntity.notFound().build();
        }
        TaxCalculationResponse response = new TaxCalculationResponse(calculation.getTaxType(), calculation.getBaseValue(), calculation.getAliquot(), calculation.getTaxAmount());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/types/{id}")
    public TaxType getTaxTypeById(@PathVariable Long id) {
        return taxService.getTaxTypeById(id);
    }

    @DeleteMapping("/types/{id}")
    public ResponseEntity<Void> deleteTaxType(@PathVariable Long id) {
        taxService.deleteTaxType(id);
        return ResponseEntity.noContent().build();
    }
}
