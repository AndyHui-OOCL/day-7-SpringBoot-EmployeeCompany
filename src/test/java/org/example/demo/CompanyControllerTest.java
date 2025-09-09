package org.example.demo;

import org.example.demo.controller.company.CompanyController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyController companyController;

    @BeforeEach
    void setUp() throws Exception {
        Field companiesField = CompanyController.class.getDeclaredField("companies");
        companiesField.setAccessible(true);
        companiesField.set(companyController, new ArrayList<>());

        Field idCounterField = CompanyController.class.getDeclaredField("idCounter");
        idCounterField.setAccessible(true);
        idCounterField.setLong(null, 0L);
    }

    @Test
    void should_create_company_when_post_given_valid_company() throws Exception {
        String requestBody = """
                {
                    "name": "Apple"
                }
                """;
        mockMvc.perform(post("/v1/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }
}
