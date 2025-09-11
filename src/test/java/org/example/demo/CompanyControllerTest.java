package org.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.demo.controller.CompanyController;
import org.example.demo.repository.company.CompanyRepository;
import org.example.demo.repository.employee.EmployeeRepository;
import org.example.demo.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

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

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() throws Exception {
        companyRepository.cleanUp();
    }

    private long createCompany(String requestBody) throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
        MvcResult mvcResult = resultActions.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        return new ObjectMapper().readTree(contentAsString).get("id").asLong();
    }

    @Test
    void should_create_company_when_post_given_valid_company() throws Exception {
        String requestBody = """
                {
                    "name": "Apple"
                }
                """;
        long resultId = createCompany(requestBody);
        mockMvc.perform(get("/v1/companies/{id}", resultId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(resultId));
    }

    @Test
    void should_get_company_when_get_given_valid_company_id() throws Exception {
        String requestBody = """
                {
                    "name": "Apple"
                }
                """;
        long resultId = createCompany(requestBody);

        mockMvc.perform(get("/v1/companies/{id}", resultId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(resultId))
                .andExpect(jsonPath("$.name").value("Apple"));
    }

    @Test
    void should_return_company_with_employees() throws Exception {
        Company company = new Company();
        company.setName("Apple");
        companyRepository.createCompany(company);

        Employee employee = new Employee();
        employee.setSalary(8000);
        employee.setAge(30);
        employee.setAge(2);
        employee.setStatus(true);
        employee.setCompanyId(company.getId());
        employeeRepository.createEmployee(employee);

        mockMvc.perform(get("/v1/companies/{id}", company.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(company.getId()))
                .andExpect(jsonPath("$.employees.length()").value(1));
    }

    @Test
    void should_throw_exception_when_get_given_invalid_company_id() throws Exception {
        String requestBody = """
                {
                    "name": "Apple"
                }
                """;
        long resultId = createCompany(requestBody);

        mockMvc.perform(get("/v1/companies/{id}", resultId + 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_get_all_companies_when_get_given_no_parameter() throws Exception {
        String requestBody1 = """
                {
                    "name": "Apple"
                }
                """;
        long resultId1 = createCompany(requestBody1);

        String requestBody2 = """
                {
                    "name": "XiaoMi"
                }
                """;
        long resultId2 = createCompany(requestBody2);

        mockMvc.perform(get("/v1/companies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(resultId1))
                .andExpect(jsonPath("$[0].name").value("Apple"))
                .andExpect(jsonPath("$[1].id").value(resultId2))
                .andExpect(jsonPath("$[1].name").value("XiaoMi"));
    }

    @Test
    void should_update_company_name_when_update_given_valid_id() throws Exception {
        String requestBody = """
                {
                    "name": "Apple"
                }
                """;
        long resultId = createCompany(requestBody);

        String updateBody = """
                {
                     "id": %d,
                     "name": "XiaoMi"
                }
                """.formatted(resultId);
        mockMvc.perform(put("/v1/companies/{id}", resultId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(resultId))
                .andExpect(jsonPath("$.name").value("XiaoMi"));
    }

    @Test
    void should_throw_exception_when_update_given_invalid_id() throws Exception {
        String requestBody = """
                {
                    "name": "Apple"
                }
                """;
        long resultId = createCompany(requestBody);

        String updateBody = """
                {
                    "name": "XiaoMi"
                }
                """;
        mockMvc.perform(put("/v1/companies/{id}", resultId + 1)
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
        long resultId = createCompany(requestBody);

        mockMvc.perform(delete("/v1/companies/{id}", resultId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_throw_exception_when_delete_given_invalid_id() throws Exception {
        String requestBody = """
                {
                    "name": "Apple"
                }
                """;
        long resultId = createCompany(requestBody);
        mockMvc.perform(delete("/v1/companies/{id}", resultId + 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_paginated_result_when_getCompanies_given_page_and_size() throws Exception {
        String requestBody1 = """
                {
                    "name": "Apple1"
                }
                """;

        createCompany(requestBody1);

        String requestBody2 = """
                {
                    "name": "Apple2"
                }
                """;
        createCompany(requestBody2);

        String requestBody3 = """
                {
                    "name": "Apple3"
                }
                """;
        createCompany(requestBody3);

        String requestBody4 = """
                {
                    "name": "Apple4"
                }
                """;
        createCompany(requestBody4);

        String requestBody5 = """
                {
                    "name": "Apple5"
                }
                """;
        createCompany(requestBody5);

        String requestBody6 = """
                {
                    "name": "Apple6"
                }
                """;
        createCompany(requestBody6);

        mockMvc.perform(get("/v1/companies?page=1&size=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].name").value("Apple1"))
                .andExpect(jsonPath("$[1].name").value("Apple2"))
                .andExpect(jsonPath("$[2].name").value("Apple3"))
                .andExpect(jsonPath("$[3].name").value("Apple4"))
                .andExpect(jsonPath("$[4].name").value("Apple5"));
    }

    @Test
    void should_throw_exception_when_getCompanies_given_invalid_page_number() throws Exception {
        String requestBody1 = """
                {
                    "name": "Apple1"
                }
                """;

        String requestBody = """
                {
                    "name": "Apple"
                }
                """;
        long resultId1 = createCompany(requestBody1);

        String requestBody2 = """
                {
                    "name": "Apple2"
                }
                """;
        long resultId2 = createCompany(requestBody2);

        String requestBody3 = """
                {
                    "name": "Apple3"
                }
                """;
        long resultId3 = createCompany(requestBody3);

        String requestBody4 = """
                {
                    "name": "Apple4"
                }
                """;
        long resultId4 = createCompany(requestBody4);

        String requestBody5 = """
                {
                    "name": "Apple5"
                }
                """;
        long resultId5 = createCompany(requestBody5);

        String requestBody6 = """
                {
                    "name": "Apple6"
                }
                """;
        long resultId6 = createCompany(requestBody6);

        mockMvc.perform(get("/v1/companies?page=-1&size=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_throw_exception_when_getCompanies_given_invalid_size_number() throws Exception {
        String requestBody1 = """
                {
                    "name": "Apple1"
                }
                """;

        String requestBody = """
                {
                    "name": "Apple"
                }
                """;
        long resultId1 = createCompany(requestBody1);

        String requestBody2 = """
                {
                    "name": "Apple2"
                }
                """;
        long resultId2 = createCompany(requestBody2);

        String requestBody3 = """
                {
                    "name": "Apple3"
                }
                """;
        long resultId3 = createCompany(requestBody3);

        String requestBody4 = """
                {
                    "name": "Apple4"
                }
                """;
        long resultId4 = createCompany(requestBody4);

        String requestBody5 = """
                {
                    "name": "Apple5"
                }
                """;
        long resultId5 = createCompany(requestBody5);

        String requestBody6 = """
                {
                    "name": "Apple6"
                }
                """;
        long resultId6 = createCompany(requestBody6);

        mockMvc.perform(get("/v1/companies?page=1&size=0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
