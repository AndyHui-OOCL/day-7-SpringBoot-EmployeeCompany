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

    @Test
    void should_get_company_when_get_given_valid_employee_id() throws Exception {
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
}
