package org.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.demo.controller.EmployeeController;
import org.example.demo.controller.request.UpdateEmployeeRequest;
import org.example.demo.repository.company.CompanyRepository;
import org.example.demo.repository.employee.EmployeeRepository;
import org.example.demo.service.EmployeeService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CompanyRepository companyRepository;

    ObjectMapper objectMapper = new ObjectMapper();
    @BeforeEach
    void setUp() {
        employeeRepository.cleanUp();
    }

    private long createEmployee(String requestBody) throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
        MvcResult mvcResult = resultActions.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        return new ObjectMapper().readTree(contentAsString).get("id").asLong();
    }

    @Test
    void should_create_employee_when_post_given_valid_employee() throws Exception {
        Company company = new Company();
        company.setName("Apple");
        companyRepository.createCompany(company);

        Employee employee = new Employee();
        employee.setName("Tom");
        employee.setSalary(21000);
        employee.setAge(30);
        employee.setGender("Male");
        employee.setStatus(true);
        employee.setCompanyId(company.getId());
        mockMvc.perform(post("/v1/employees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated());
    }

    @Test
    void should_throw_error_when_post_given_invalid_age_range() throws Exception {
        Company company = new Company();
        company.setName("Apple");
        companyRepository.createCompany(company);

        Employee employee = new Employee();
        employee.setName("Tom");
        employee.setSalary(21000);
        employee.setAge(12);
        employee.setGender("Male");
        employee.setStatus(true);
        employee.setCompanyId(company.getId());

        mockMvc.perform(post("/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_create_employee_when_post_given_invalid_age_and_salary_range() throws Exception {
        Company company = new Company();
        company.setName("Apple");
        companyRepository.createCompany(company);

        Employee employee = new Employee();
        employee.setName("Tom");
        employee.setSalary(17000);
        employee.setAge(30);
        employee.setGender("Male");
        employee.setStatus(true);
        employee.setCompanyId(company.getId());

        mockMvc.perform(post("/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_create_employee_when_post_given_duplicated_employee() throws Exception {
        Company company = new Company();
        company.setName("Apple");
        companyRepository.createCompany(company);

        Employee employee = new Employee();
        employee.setName("Tom");
        employee.setSalary(17000);
        employee.setAge(30);
        employee.setGender("Male");
        employee.setStatus(true);
        employee.setCompanyId(company.getId());
        mockMvc.perform(post("/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)));
        mockMvc.perform(post("/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_get_employee_when_get_given_valid_employee_id() throws Exception {
        Company company = new Company();
        company.setName("Apple");
        companyRepository.createCompany(company);

        Employee employee = new Employee();
        employee.setName("Tom");
        employee.setSalary(25000);
        employee.setAge(30);
        employee.setGender("Male");
        employee.setStatus(true);
        employee.setCompanyId(company.getId());

        long resultId = createEmployee(objectMapper.writeValueAsString(employee));


        mockMvc.perform(get("/v1/employees/{id}", resultId)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(resultId))
                .andExpect(jsonPath("$.name").value(employee.getName()))
                .andExpect(jsonPath("$.age").value(employee.getAge()))
                .andExpect(jsonPath("$.gender").value(employee.getGender()))
                .andExpect(jsonPath("$.salary").value(employee.getSalary()))
                .andExpect(jsonPath("$.status").value(true));
    }

    @Test
    void should_receive_not_found_when_get_given_invalid_employee_id() throws Exception {
        Company company = new Company();
        company.setName("Apple");
        companyRepository.createCompany(company);

        Employee employee = new Employee();
        employee.setName("Tom");
        employee.setSalary(25000);
        employee.setAge(30);
        employee.setGender("Male");
        employee.setStatus(true);
        employee.setCompanyId(company.getId());

        long resultId = createEmployee(objectMapper.writeValueAsString(employee));


        mockMvc.perform(get("/v1/employees/{id}",  resultId + 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_get_male_employee_when_get_given_male_query() throws Exception {
        Company company = new Company();
        company.setName("Apple");
        companyRepository.createCompany(company);

        Employee employee = new Employee();
        employee.setName("Tom");
        employee.setSalary(25000);
        employee.setAge(30);
        employee.setGender("Male");
        employee.setStatus(true);
        employee.setCompanyId(company.getId());

        long resultId = createEmployee(objectMapper.writeValueAsString(employee));

        mockMvc.perform(get("/v1/employees?gender=Male")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(resultId))
                .andExpect(jsonPath("$[0].name").value(employee.getName()))
                .andExpect(jsonPath("$[0].age").value(employee.getAge()))
                .andExpect(jsonPath("$[0].gender").value(employee.getGender()))
                .andExpect(jsonPath("$[0].salary").value(employee.getSalary()))
                .andExpect(jsonPath("$[0].status").value(true));
    }

    @Test
    void should_get_no_employee_when_get_given_female_query() throws Exception {
        mockMvc.perform(get("/v1/employees?gender=Female")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void should_get_all_employee_when_getAll_given_2_employee() throws Exception {
        String malEmployeeJson = """
                {
                    "name": "John Smith",
                    "age": 34,
                    "salary": 25000.0,
                    "gender": "Male",
                    "status": true
                }
                """;
        long resultId1 = createEmployee(malEmployeeJson);

        String femaleEmployeeJson = """
                {
                    "name": "Mary",
                    "age": 31,
                    "salary": 25000.0,
                    "gender": "Female",
                    "status": true
                }
                """;
        long resultId2 = createEmployee(femaleEmployeeJson);

        mockMvc.perform(get("/v1/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(resultId1))
                .andExpect(jsonPath("$[0].name").value("John Smith"))
                .andExpect(jsonPath("$[0].age").value(34))
                .andExpect(jsonPath("$[0].gender").value("Male"))
                .andExpect(jsonPath("$[0].salary").value(25000.0))
                .andExpect(jsonPath("$[0].status").value(true))
                .andExpect(jsonPath("$[1].id").value(resultId2))
                .andExpect(jsonPath("$[1].name").value("Mary"))
                .andExpect(jsonPath("$[1].age").value(31))
                .andExpect(jsonPath("$[1].gender").value("Female"))
                .andExpect(jsonPath("$[1].salary").value(25000.0))
                .andExpect(jsonPath("$[1].status").value(true));
    }

    @Test
    void should_update_employee_when_update_given_valid_id() throws Exception {
        Company company = new Company();
        company.setName("Apple");
        companyRepository.createCompany(company);

        Employee employee = new Employee();
        employee.setName("Tom");
        employee.setSalary(21000);
        employee.setAge(30);
        employee.setGender("Male");
        employee.setStatus(true);
        employee.setCompanyId(company.getId());
        employeeRepository.createEmployee(employee);

        UpdateEmployeeRequest updateEmployeeRequest = new UpdateEmployeeRequest();
        updateEmployeeRequest.setName("Tom");
        updateEmployeeRequest.setSalary(21000);
        updateEmployeeRequest.setAge(35);
        updateEmployeeRequest.setGender("Male");

        mockMvc.perform(put("/v1/employees/{id}", employee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateEmployeeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employee.getId()))
                .andExpect(jsonPath("$.name").value(updateEmployeeRequest.getName()))
                .andExpect(jsonPath("$.age").value(updateEmployeeRequest.getAge()))
                .andExpect(jsonPath("$.salary").value(updateEmployeeRequest.getSalary()))
                .andExpect(jsonPath("$.gender").value(updateEmployeeRequest.getGender()))
                .andExpect(jsonPath("$.status").value(true));
    }

    @Test
    void should_return_not_found_when_update_given_non_active_user() throws Exception {
        String requestBody = """
                {
                    "name": "John Smith",
                    "age": 34,
                    "salary": 25000.0,
                    "gender": "Male"
                }
                """;
        long resultId = createEmployee(requestBody);

        mockMvc.perform(delete("/v1/employees/{id}", resultId)
                .contentType(MediaType.APPLICATION_JSON));


        String updateBody = """
            {
                "name" : "John Smith",
                "age": 30,
                "salary": 25000.0,
                "gender": "Male"
            }
            """;
        mockMvc.perform(put("/v1/employees/{id}", resultId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_not_found_when_update_given_non_existing_user() throws Exception {
        String updateBody = """
            {
                "name" : "John Smith",
                "age": 30,
                "salary": 25000.0,
                "gender": "Male"
            }
            """;
        mockMvc.perform(put("/v1/employees/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_delete_employee_when_delete_given_valid_id() throws Exception {
        String requestBody = """
                {
                    "name": "Mary",
                    "age": 31,
                    "salary": 25000.0,
                    "gender": "Female"
                }
                """;
        long resultId = createEmployee(requestBody);

        mockMvc.perform(delete("/v1/employees/{id}", resultId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_delete_employee_when_delete_given_invalid_id() throws Exception {

        mockMvc.perform(delete("/v1/employees/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_paginated_result_when_getEmployee_given_page_and_size() throws Exception {
        String requestBody1 = """
                {
                    "name": "John Smith1",
                    "age": 34,
                    "salary": 25000.0,
                    "gender": "Male"
                }
                """;
        long resultId1 = createEmployee(requestBody1);

        String requestBody2 = """
                {
                    "name": "John Smith2",
                    "age": 34,
                    "salary": 25000.0,
                    "gender": "Male"
                }
                """;
        long resultId2 = createEmployee(requestBody2);

        String requestBody3 = """
                {
                    "name": "John Smith3",
                    "age": 34,
                    "salary": 25000.0,
                    "gender": "Male"
                }
                """;
        long resultId3 = createEmployee(requestBody3);

        String requestBody4 = """
                {
                    "name": "John Smith4",
                    "age": 34,
                    "salary": 25000.0,
                    "gender": "Male"
                }
                """;
        long resultId4 = createEmployee(requestBody4);

        String requestBody5 = """
                {
                    "name": "John Smith5",
                    "age": 34,
                    "salary": 25000.0,
                    "gender": "Male"
                }
                """;
        long resultId5 = createEmployee(requestBody5);

        String requestBody6 = """
                {
                    "name": "John Smith6",
                    "age": 34,
                    "salary": 25000.0,
                    "gender": "Male"
                }
                """;
        long resultId6 = createEmployee(requestBody6);

        mockMvc.perform(get("/v1/employees?page=1&size=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].name").value("John Smith1"))
                .andExpect(jsonPath("$[1].name").value("John Smith2"))
                .andExpect(jsonPath("$[2].name").value("John Smith3"))
                .andExpect(jsonPath("$[3].name").value("John Smith4"))
                .andExpect(jsonPath("$[4].name").value("John Smith5"));
    }

    @Test
    void should_throw_error_when_getEmployee_given_invalid_size_number() throws Exception {
        String requestBody1 = """
                {
                    "name": "John Smith1",
                    "age": 34,
                    "salary": 25000.0,
                    "gender": "Male"
                }
                """;
        long resultId1 = createEmployee(requestBody1);

        String requestBody2 = """
                {
                    "name": "John Smith2",
                    "age": 34,
                    "salary": 25000.0,
                    "gender": "Male"
                }
                """;
        long resultId2 = createEmployee(requestBody2);

        String requestBody3 = """
                {
                    "name": "John Smith3",
                    "age": 34,
                    "salary": 25000.0,
                    "gender": "Male"
                }
                """;
        long resultId3 = createEmployee(requestBody3);

        String requestBody4 = """
                {
                    "name": "John Smith4",
                    "age": 34,
                    "salary": 25000.0,
                    "gender": "Male"
                }
                """;
        long resultId4 = createEmployee(requestBody4);

        String requestBody5 = """
                {
                    "name": "John Smith5",
                    "age": 34,
                    "salary": 25000.0,
                    "gender": "Male"
                }
                """;
        long resultId5 = createEmployee(requestBody5);

        String requestBody6 = """
                {
                    "name": "John Smith6",
                    "age": 34,
                    "salary": 25000.0,
                    "gender": "Male"
                }
                """;
        long resultId6 = createEmployee(requestBody6);

        mockMvc.perform(get("/v1/employees?page=-1&size=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_throw_error_when_getEmployee_given_invalid_page_number() throws Exception {
        String requestBody1 = """
                {
                    "name": "John Smith1",
                    "age": 34,
                    "salary": 25000.0,
                    "gender": "Male"
                }
                """;
        long resultId1 = createEmployee(requestBody1);

        String requestBody2 = """
                {
                    "name": "John Smith2",
                    "age": 34,
                    "salary": 25000.0,
                    "gender": "Male"
                }
                """;
        long resultId2 = createEmployee(requestBody2);

        String requestBody3 = """
                {
                    "name": "John Smith3",
                    "age": 34,
                    "salary": 25000.0,
                    "gender": "Male"
                }
                """;
        long resultId3 = createEmployee(requestBody3);

        String requestBody4 = """
                {
                    "name": "John Smith4",
                    "age": 34,
                    "salary": 25000.0,
                    "gender": "Male"
                }
                """;
        long resultId4 = createEmployee(requestBody4);

        String requestBody5 = """
                {
                    "name": "John Smith5",
                    "age": 34,
                    "salary": 25000.0,
                    "gender": "Male"
                }
                """;
        long resultId5 = createEmployee(requestBody5);

        String requestBody6 = """
                {
                    "name": "John Smith6",
                    "age": 34,
                    "salary": 25000.0,
                    "gender": "Male"
                }
                """;
        long resultId6 = createEmployee(requestBody6);

        mockMvc.perform(get("/v1/employees?page=1&size=0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
