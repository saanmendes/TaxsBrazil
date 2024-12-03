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

//    @InjectMocks
//    private TaxesController taxesController;

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
        // Cria um novo tipo de imposto com os dados de entrada (sem id, pois está sendo criado)
        TaxType newTaxType = new TaxType(null, "ISS", "Tax on services");

        // Cria o tipo de imposto que será retornado após a criação, já com o id atribuído (id = 3)
        TaxType createdTaxType = new TaxType(3L, "ISS", "Tax on services");

        // WHEN: Ação - Simula a chamada do método de serviço que cria um novo tipo de imposto
        // 'when' define o comportamento esperado do mock 'taxService' quando o método 'createTaxType' for chamado
        // Ele vai retornar o objeto 'createdTaxType' que simulamos acima
        when(taxService.createTaxType(any(TaxType.class))).thenReturn(createdTaxType);

        //  Verificação - Fazemos uma requisição HTTP e verificamos a resposta
        mockMvc.perform(post("/api/taxes/types")  // Realiza a requisição HTTP POST no endpoint
                        .contentType("application/json")  // Define o tipo de conteúdo da requisição como JSON
                        .content("{\"name\": \"ISS\", \"description\": \"Tax on services\"}"))  // Corpo da requisição com os dados

                // Verifica se o status da resposta é 200 OK
                .andExpect(status().isOk())

                // Verifica se o 'id' retornado no JSON é igual a 3
                .andExpect(jsonPath("$.id").value(3))

                // Verifica se o 'name' retornado no JSON é igual a "ISS"
                .andExpect(jsonPath("$.name").value("ISS"))

                // Verifica se a 'description' retornada no JSON é igual a "Tax on services"
                .andExpect(jsonPath("$.description").value("Tax on services"));
    }

//    @Test
//    void testCaseCorrectCalculation() {
//        Double result = taxController.calculationTest(100.0);
//    }

   @Test
    public void testCaseCorrectCalculation() throws Exception {
       // Criando a requisição com o taxTypeId e baseValue
       TaxCalculationRequest calculationTaxRequest = new TaxCalculationRequest();
       calculationTaxRequest.setTaxTypeId(1L);
       calculationTaxRequest.setBaseValue(1000);
//
//        // Convertendo o objeto para JSON
       String json = mapper.writeValueAsString(calculationTaxRequest);
//
//        // Criando um objeto TaxCalculation para simular a resposta do serviço
       TaxCalculation taxCalculation = new TaxCalculation("IPI", 1000, 12, 120);
//
//        // Simulando o comportamento do serviço para retornar um TaxCalculation
      Mockito.when(taxService.calculateTax(any(TaxCalculationRequest.class))).thenReturn(taxCalculation);
//
//        // Realizando a requisição e verificando a resposta com MockMvc
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
