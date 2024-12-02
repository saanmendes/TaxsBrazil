package com.tax.services;

import com.tax.controllers.dtos.TaxCalculationRequest;
import com.tax.exception.ResourceNotFoundException;
import com.tax.models.TaxCalculation;
import com.tax.models.TaxType;
import com.tax.repositories.TaxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TaxService {

    @Autowired
    private TaxRepository taxRepository;

    public TaxCalculation calculateTax(TaxCalculationRequest request) {
        Optional<TaxType> taxTypeOpt = taxRepository.findById(request.getTaxTypeId());
        if (!taxTypeOpt.isPresent()) {
            return null;
        }

        TaxType taxType = taxTypeOpt.get();
        double taxAmount = request.getBaseValue() * (taxType.getAliquot() / 100);
        return new TaxCalculation(taxType.getName(), request.getBaseValue(), taxType.getAliquot(), taxAmount);
    }

    public List<TaxType> getAllTaxTypes() {
        return taxRepository.findAll();
    }

    public TaxType getTaxTypeById(Long id) {
        return taxRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tax type not found"));
    }

    public TaxType createTaxType(TaxType taxType) {
        return taxRepository.save(taxType);
    }

    public void deleteTaxType(Long id) {
        if (!taxRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tax type not found");
        }
        taxRepository.deleteById(id);
    }
}
