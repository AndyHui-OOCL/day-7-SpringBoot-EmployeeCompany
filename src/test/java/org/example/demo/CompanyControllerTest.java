package org.example.demo;

import org.example.demo.controller.CompanyController;
import org.example.demo.repository.CompanyRepository;
import org.example.demo.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyController companyController;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyRepository companyRepository;

    @BeforeEach
    void setUp() throws Exception {
        Field companiesField =CompanyRepository.class.getDeclaredField("companies");
        companiesField.setAccessible(true);
        companiesField.set(companyRepository, new ArrayList<>());

        Field idCounterField = CompanyRepository.class.getDeclaredField("idCounter");
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

    @Test
    void should_get_company_when_get_given_valid_company_id() throws Exception {
        String requestBody = """
                {
                    "name": "Apple"
                }
                """;
        mockMvc.perform(post("/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        mockMvc.perform(get("/v1/companies/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Apple"));
    }

    @Test
    void should_get_company_when_get_given_invalid_company_id() throws Exception {
        String requestBody = """
                {
                    "name": "Apple"
                }
                """;
        mockMvc.perform(post("/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        mockMvc.perform(get("/v1/companies/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_get_all_companies_when_get_given_given_no_parameter() throws Exception {
        String requestBody1 = """
                {
                    "name": "Apple"
                }
                """;
        mockMvc.perform(post("/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody1));

        String requestBody = """
                {
                    "name": "XiaoMi"
                }
                """;
        mockMvc.perform(post("/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        mockMvc.perform(get("/v1/companies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Apple"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("XiaoMi"));
    }

    @Test
    void should_update_company_name_when_update_given_valid_id() throws Exception {
        String requestBody = """
                {
                    "name": "Apple"
                }
                """;
        mockMvc.perform(post("/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        String updateBody = """
                {
                    "name": "XiaoMi"
                }
                """;
        mockMvc.perform(put("/v1/companies/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("XiaoMi"));
    }

    @Test
    void should_throw_error_when_update_given_invalid_id() throws Exception {
        String requestBody = """
                {
                    "name": "Apple"
                }
                """;
        mockMvc.perform(post("/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        String updateBody = """
                {
                    "name": "XiaoMi"
                }
                """;
        mockMvc.perform(put("/v1/companies/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_delete_company_when_delete_given_valid_id() throws Exception {
        String requestBody = """
                {
                    "name": "Apple"
                }
                """;
        mockMvc.perform(post("/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        mockMvc.perform(delete("/v1/companies/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_return_paginated_result_when_getCompanies_given_page_and_size() throws Exception {
        String requestBody1 = """
                {
                    "name": "Apple1"
                }
                """;

        mockMvc.perform(post("/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody1));

        String requestBody2 = """
                {
                    "name": "Apple2"
                }
                """;
        mockMvc.perform(post("/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody2));

        String requestBody3 = """
                {
                    "name": "Apple3"
                }
                """;
        mockMvc.perform(post("/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody3));

        String requestBody4 = """
                {
                    "name": "Apple4"
                }
                """;
        mockMvc.perform(post("/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody4));

        String requestBody5 = """
                {
                    "name": "Apple5"
                }
                """;
        mockMvc.perform(post("/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody5));

        String requestBody6 = """
                {
                    "name": "Apple6"
                }
                """;
        mockMvc.perform(post("/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody6));

        mockMvc.perform(get("/v1/companies?page=1&size=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].name").value("Apple2"))
                .andExpect(jsonPath("$[1].name").value("Apple3"))
                .andExpect(jsonPath("$[2].name").value("Apple4"))
                .andExpect(jsonPath("$[3].name").value("Apple5"))
                .andExpect(jsonPath("$[4].name").value("Apple6"));
    }

    @Test
    void should_throw_error_when_getCompanies_given_invalid_page_number() throws Exception {
        String requestBody1 = """
                {
                    "name": "Apple1"
                }
                """;

        mockMvc.perform(post("/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody1));

        String requestBody2 = """
                {
                    "name": "Apple2"
                }
                """;
        mockMvc.perform(post("/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody2));

        String requestBody3 = """
                {
                    "name": "Apple3"
                }
                """;
        mockMvc.perform(post("/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody3));

        String requestBody4 = """
                {
                    "name": "Apple4"
                }
                """;
        mockMvc.perform(post("/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody4));

        String requestBody5 = """
                {
                    "name": "Apple5"
                }
                """;
        mockMvc.perform(post("/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody5));

        String requestBody6 = """
                {
                    "name": "Apple6"
                }
                """;
        mockMvc.perform(post("/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody6));

        mockMvc.perform(get("/v1/companies?page=-1&size=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_throw_error_when_getCompanies_given_invalid_size_number() throws Exception {
        String requestBody1 = """
                {
                    "name": "Apple1"
                }
                """;

        mockMvc.perform(post("/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody1));

        String requestBody2 = """
                {
                    "name": "Apple2"
                }
                """;
        mockMvc.perform(post("/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody2));

        String requestBody3 = """
                {
                    "name": "Apple3"
                }
                """;
        mockMvc.perform(post("/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody3));

        String requestBody4 = """
                {
                    "name": "Apple4"
                }
                """;
        mockMvc.perform(post("/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody4));

        String requestBody5 = """
                {
                    "name": "Apple5"
                }
                """;
        mockMvc.perform(post("/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody5));

        String requestBody6 = """
                {
                    "name": "Apple6"
                }
                """;
        mockMvc.perform(post("/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody6));

        mockMvc.perform(get("/v1/companies?page=1&size=0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
