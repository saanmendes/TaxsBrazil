package com.tax.services;

import com.tax.TaxApplication;
import com.tax.exception.ResourceNotFoundException;
import com.tax.models.TaxCalculation;
import com.tax.controllers.dtos.TaxCalculationRequest;
import com.tax.models.TaxType;
import com.tax.repositories.TaxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest(classes = TaxApplication.class)
public class TaxServiceTest {

    @Mock
    private TaxRepository taxRepository;

    @InjectMocks
    private TaxService taxService;

    private TaxType taxType;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        taxType = new TaxType(null, "ISS", "Tax on services");
    }

    @Test
    void testCalculateTax_TaxTypeNotFound() {
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.setTaxTypeId(1L);
        request.setBaseValue(1000.0);

        when(taxRepository.findById(1L)).thenReturn(Optional.empty());

        TaxCalculation result = taxService.calculateTax(request);

        assertNull(result);
    }

    @Test
    public void testCreateTaxType() {
        TaxType taxType = new TaxType(null, "ISS", "Tax on services");
        taxType.setId(1L);
        taxType.setAliquot(0.18);

        when(taxRepository.save(taxType)).thenReturn(taxType);

        TaxType createdTaxType = taxService.createTaxType(taxType);

        assertNotNull(createdTaxType);
        assertEquals(taxType.getId(), createdTaxType.getId());
        assertEquals(taxType.getAliquot(), createdTaxType.getAliquot(), "The tax rates should be the same");

        verify(taxRepository, times(1)).save(taxType);
    }

    @Test
    void testGetTaxTypeById_NotFound() {
        when(taxRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taxService.getTaxTypeById(1L));
    }

    @Test
    void testDeleteTaxType_Success() {
        when(taxRepository.existsById(1L)).thenReturn(true);

        taxService.deleteTaxType(1L);

        verify(taxRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTaxType_NotFound() {
        when(taxRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> taxService.deleteTaxType(1L));
    }
}
