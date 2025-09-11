package org.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.demo.controller.request.UpdateEmployeeRequest;
import org.example.demo.repository.company.CompanyRepository;
import org.example.demo.repository.employee.EmployeeRepository;
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
        Company company = new Company();
        company.setName("Apple");
        companyRepository.createCompany(company);

        Employee employee1 = new Employee();
        employee1.setName("Tom");
        employee1.setSalary(25000);
        employee1.setAge(30);
        employee1.setGender("Male");
        employee1.setStatus(true);
        employee1.setCompanyId(company.getId());

        long resultId1 = createEmployee(objectMapper.writeValueAsString(employee1));

        Employee employee2 = new Employee();
        employee2.setName("Jane");
        employee2.setSalary(25000);
        employee2.setAge(30);
        employee2.setGender("Female");
        employee2.setStatus(true);
        employee2.setCompanyId(company.getId());

        long resultId2 = createEmployee(objectMapper.writeValueAsString(employee2));

        mockMvc.perform(get("/v1/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(resultId1))
                .andExpect(jsonPath("$[0].name").value(employee1.getName()))
                .andExpect(jsonPath("$[0].age").value(employee1.getAge()))
                .andExpect(jsonPath("$[0].gender").value(employee1.getGender()))
                .andExpect(jsonPath("$[0].salary").value(employee1.getSalary()))
                .andExpect(jsonPath("$[0].status").value(true))
                .andExpect(jsonPath("$[1].id").value(resultId2))
                .andExpect(jsonPath("$[1].name").value(employee2.getName()))
                .andExpect(jsonPath("$[1].age").value(employee2.getAge()))
                .andExpect(jsonPath("$[1].gender").value(employee2.getGender()))
                .andExpect(jsonPath("$[1].salary").value(employee2.getSalary()))
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

        mockMvc.perform(delete("/v1/employees/{id}", resultId)
                .contentType(MediaType.APPLICATION_JSON));

        UpdateEmployeeRequest updateEmployeeRequest = new UpdateEmployeeRequest();
        updateEmployeeRequest.setName("Tom");
        updateEmployeeRequest.setSalary(21000);
        updateEmployeeRequest.setAge(35);
        updateEmployeeRequest.setGender("Male");

        mockMvc.perform(put("/v1/employees/{id}", resultId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateEmployeeRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_not_found_when_update_given_non_existing_user() throws Exception {
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

        UpdateEmployeeRequest updateEmployeeRequest = new UpdateEmployeeRequest();
        updateEmployeeRequest.setName("Tom");
        updateEmployeeRequest.setSalary(21000);
        updateEmployeeRequest.setAge(35);
        updateEmployeeRequest.setGender("Male");

        mockMvc.perform(put("/v1/employees/{id}", resultId + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateEmployeeRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_delete_employee_when_delete_given_valid_id() throws Exception {
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

        mockMvc.perform(delete("/v1/employees/{id}", resultId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_delete_employee_when_delete_given_invalid_id() throws Exception {
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

        mockMvc.perform(delete("/v1/employees/{id}", resultId + 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_paginated_result_when_getEmployee_given_page_and_size() throws Exception {
        Company company = new Company();
        company.setName("Apple");
        companyRepository.createCompany(company);

        Employee employee1 = new Employee();
        employee1.setName("Tom1");
        employee1.setSalary(25000);
        employee1.setAge(30);
        employee1.setGender("Male");
        employee1.setStatus(true);
        employee1.setCompanyId(company.getId());

        long resultId = createEmployee(objectMapper.writeValueAsString(employee1));

        Employee employee2 = new Employee();
        employee2.setName("Tom2");
        employee2.setSalary(25000);
        employee2.setAge(30);
        employee2.setGender("Male");
        employee2.setStatus(true);
        employee2.setCompanyId(company.getId());
        createEmployee(objectMapper.writeValueAsString(employee2));

        Employee employee3 = new Employee();
        employee3.setName("Tom3");
        employee3.setSalary(25000);
        employee3.setAge(30);
        employee3.setGender("Male");
        employee3.setStatus(true);
        employee3.setCompanyId(company.getId());
        createEmployee(objectMapper.writeValueAsString(employee3));

        Employee employee4 = new Employee();
        employee4.setName("Tom4");
        employee4.setSalary(25000);
        employee4.setAge(30);
        employee4.setGender("Male");
        employee4.setStatus(true);
        employee4.setCompanyId(company.getId());
        createEmployee(objectMapper.writeValueAsString(employee4));

        Employee employee5 = new Employee();
        employee5.setName("Tom5");
        employee5.setSalary(25000);
        employee5.setAge(30);
        employee5.setGender("Male");
        employee5.setStatus(true);
        employee5.setCompanyId(company.getId());
        createEmployee(objectMapper.writeValueAsString(employee5));

        Employee employee6 = new Employee();
        employee6.setName("Tom6");
        employee6.setSalary(25000);
        employee6.setAge(30);
        employee6.setGender("Male");
        employee6.setStatus(true);
        employee6.setCompanyId(company.getId());
        createEmployee(objectMapper.writeValueAsString(employee6));

        mockMvc.perform(get("/v1/employees?page=1&size=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].name").value("Tom1"))
                .andExpect(jsonPath("$[1].name").value("Tom2"))
                .andExpect(jsonPath("$[2].name").value("Tom3"))
                .andExpect(jsonPath("$[3].name").value("Tom4"))
                .andExpect(jsonPath("$[4].name").value("Tom5"));
    }

    @Test
    void should_throw_error_when_getEmployee_given_invalid_size_number() throws Exception {
        Company company = new Company();
        company.setName("Apple");
        companyRepository.createCompany(company);

        Employee employee1 = new Employee();
        employee1.setName("Tom1");
        employee1.setSalary(25000);
        employee1.setAge(30);
        employee1.setGender("Male");
        employee1.setStatus(true);
        employee1.setCompanyId(company.getId());

        createEmployee(objectMapper.writeValueAsString(employee1));

        Employee employee2 = new Employee();
        employee2.setName("Tom2");
        employee2.setSalary(25000);
        employee2.setAge(30);
        employee2.setGender("Male");
        employee2.setStatus(true);
        employee2.setCompanyId(company.getId());
        createEmployee(objectMapper.writeValueAsString(employee2));

        Employee employee3 = new Employee();
        employee3.setName("Tom3");
        employee3.setSalary(25000);
        employee3.setAge(30);
        employee3.setGender("Male");
        employee3.setStatus(true);
        employee3.setCompanyId(company.getId());
        createEmployee(objectMapper.writeValueAsString(employee3));

        Employee employee4 = new Employee();
        employee4.setName("Tom4");
        employee4.setSalary(25000);
        employee4.setAge(30);
        employee4.setGender("Male");
        employee4.setStatus(true);
        employee4.setCompanyId(company.getId());
        createEmployee(objectMapper.writeValueAsString(employee4));

        Employee employee5 = new Employee();
        employee5.setName("Tom5");
        employee5.setSalary(25000);
        employee5.setAge(30);
        employee5.setGender("Male");
        employee5.setStatus(true);
        employee5.setCompanyId(company.getId());
        createEmployee(objectMapper.writeValueAsString(employee5));

        Employee employee6 = new Employee();
        employee6.setName("Tom6");
        employee6.setSalary(25000);
        employee6.setAge(30);
        employee6.setGender("Male");
        employee6.setStatus(true);
        employee6.setCompanyId(company.getId());
        createEmployee(objectMapper.writeValueAsString(employee6));

        mockMvc.perform(get("/v1/employees?page=-1&size=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_throw_error_when_getEmployee_given_invalid_page_number() throws Exception {
        Company company = new Company();
        company.setName("Apple");
        companyRepository.createCompany(company);

        Employee employee1 = new Employee();
        employee1.setName("Tom1");
        employee1.setSalary(25000);
        employee1.setAge(30);
        employee1.setGender("Male");
        employee1.setStatus(true);
        employee1.setCompanyId(company.getId());

        createEmployee(objectMapper.writeValueAsString(employee1));

        Employee employee2 = new Employee();
        employee2.setName("Tom2");
        employee2.setSalary(25000);
        employee2.setAge(30);
        employee2.setGender("Male");
        employee2.setStatus(true);
        employee2.setCompanyId(company.getId());
        createEmployee(objectMapper.writeValueAsString(employee2));

        Employee employee3 = new Employee();
        employee3.setName("Tom3");
        employee3.setSalary(25000);
        employee3.setAge(30);
        employee3.setGender("Male");
        employee3.setStatus(true);
        employee3.setCompanyId(company.getId());
        createEmployee(objectMapper.writeValueAsString(employee3));

        Employee employee4 = new Employee();
        employee4.setName("Tom4");
        employee4.setSalary(25000);
        employee4.setAge(30);
        employee4.setGender("Male");
        employee4.setStatus(true);
        employee4.setCompanyId(company.getId());
        createEmployee(objectMapper.writeValueAsString(employee4));

        Employee employee5 = new Employee();
        employee5.setName("Tom5");
        employee5.setSalary(25000);
        employee5.setAge(30);
        employee5.setGender("Male");
        employee5.setStatus(true);
        employee5.setCompanyId(company.getId());
        createEmployee(objectMapper.writeValueAsString(employee5));

        Employee employee6 = new Employee();
        employee6.setName("Tom6");
        employee6.setSalary(25000);
        employee6.setAge(30);
        employee6.setGender("Male");
        employee6.setStatus(true);
        employee6.setCompanyId(company.getId());
        createEmployee(objectMapper.writeValueAsString(employee6));

        mockMvc.perform(get("/v1/employees?page=1&size=0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
