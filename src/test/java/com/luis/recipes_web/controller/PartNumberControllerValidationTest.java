package com.luis.recipes_web.controller;

import com.luis.recipes_web.service.PartNumberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PartNumberController.class)
class PartNumberControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartNumberService partNumberService;

    @Test
    void create_shouldReturn400_whenBodyIsInvalid() throws Exception {
        String json = """
                {
                  "codigoPartNumber": "",
                  "nombrePartNumber": "   ",
                  "activo": null
                }
                """;

        mockMvc.perform(post("/api/part-numbers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/part-numbers"))
                .andExpect(jsonPath("$.fieldErrors.codigoPartNumber").isArray())
                .andExpect(jsonPath("$.fieldErrors.nombrePartNumber").isArray())
                .andExpect(jsonPath("$.fieldErrors.activo").isArray());

        // En inválido, el service no debería ejecutarse
        verifyNoInteractions(partNumberService);
    }
}
