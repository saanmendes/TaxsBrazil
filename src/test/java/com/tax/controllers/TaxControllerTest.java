package com.tax.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tax.TaxApplication;
import com.tax.controllers.dtos.TaxCalculationRequest;
import com.tax.controllers.dtos.TaxCalculationResponse;
import com.tax.models.TaxCalculation;
import com.tax.models.TaxType;
import com.tax.services.TaxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = TaxApplication.class)
@AutoConfigureMockMvc
class TaxControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaxService taxService;

    private ObjectMapper mapper;
    private TaxCalculationResponse calculationTaxResponseDto;

    @BeforeEach
    public void setUp() {
        mapper = new ObjectMapper();

        calculationTaxResponseDto = new TaxCalculationResponse
                ("IPI", 1000, 12, 120);
    }


    @Test
    void testCreateTaxType() throws Exception {
        TaxType newTaxType = new TaxType(3L, "ISS", "Tax on services");

        TaxType createdTaxType = new TaxType(3L, "ISS", "Tax on services");

        when(taxService.createTaxType(any(TaxType.class))).thenReturn(createdTaxType);


        mockMvc.perform(post("/api/taxes/types")
                        .contentType("application/json")
                        .content("{\"name\": \"ISS\", \"description\": \"Tax on services\"}"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("ISS"))
                .andExpect(jsonPath("$.description").value("Tax on services"));
    }


   @Test
    public void testCaseCorrectCalculation() throws Exception {
       TaxCalculationRequest calculationTaxRequest = new TaxCalculationRequest();
       calculationTaxRequest.setTaxTypeId(1L);
       calculationTaxRequest.setBaseValue(1000);

       String json = mapper.writeValueAsString(calculationTaxRequest);

       TaxCalculation taxCalculation = new TaxCalculation("IPI", 1000, 12, 120);

      Mockito.when(taxService.calculateTax(any(TaxCalculationRequest.class))).thenReturn(taxCalculation);
        mockMvc.perform(
                      MockMvcRequestBuilders
                                .post("/api/taxes/calculation")
                                .contentType(MediaType.APPLICATION_JSON)
                               .content(json))
               .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.taxType").value(calculationTaxResponseDto.getTaxType()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.baseValue").value(1000.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.aliquot").value(12.0))
              .andExpect(MockMvcResultMatchers.jsonPath("$.taxAmount").value(120.0));
    }
}
