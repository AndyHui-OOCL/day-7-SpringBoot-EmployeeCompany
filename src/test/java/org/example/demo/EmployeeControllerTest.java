package org.example.demo;

import org.example.demo.controller.employee.EmployeeController;
import org.example.demo.repository.EmployeeRepository;
import org.example.demo.service.CompanyService;
import org.example.demo.service.EmployeeService;
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
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeController employeeController;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() throws Exception {
        Field companiesField = EmployeeRepository.class.getDeclaredField("employees");
        companiesField.setAccessible(true);
        companiesField.set(employeeRepository, new ArrayList<>());

        Field idCounterField = EmployeeRepository.class.getDeclaredField("idCounter");
        idCounterField.setAccessible(true);
        idCounterField.setLong(null, 0L);
    }

    @Test
    void should_create_employee_when_post_given_valid_employee() throws Exception {
        String requestBody = """
                {
                    "name": "John Smith",
                    "age": 34,
                    "salary": 5000.0,
                    "gender": "Male"
                }
                """;
        mockMvc.perform(post("/v1/employees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void should_get_employee_when_get_given_valid_employee_id() throws Exception {
        String requestBody = """
                {
                    "name": "John Smith",
                    "age": 34,
                    "salary": 5000.0,
                    "gender": "Male"
                }
                """;
        mockMvc.perform(post("/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));


        mockMvc.perform(get("/v1/employees/{id}", 1)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.age").value(34))
                .andExpect(jsonPath("$.gender").value("Male"))
                .andExpect(jsonPath("$.salary").value(5000.0));

    }

    @Test
    void should_get_male_employee_when_get_given_male_query() throws Exception {
        String requestBody = """
                {
                    "name": "John Smith",
                    "age": 34,
                    "salary": 5000.0,
                    "gender": "Male"
                }
                """;
        mockMvc.perform(post("/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        mockMvc.perform(get("/v1/employees?gender=Male")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Smith"))
                .andExpect(jsonPath("$[0].age").value(34))
                .andExpect(jsonPath("$[0].gender").value("Male"))
                .andExpect(jsonPath("$[0].salary").value(5000.0));
    }

    @Test
    void should_get_no_employee_when_get_given_female_query() throws Exception {
        String requestBody = """
                {
                    "name": "John Smith",
                    "age": 34,
                    "salary": 5000.0,
                    "gender": "Male"
                }
                """;
        mockMvc.perform(post("/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

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
                    "salary": 5000.0,
                    "gender": "Male"
                }
                """;
        mockMvc.perform(post("/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malEmployeeJson));

        String femaleEmployeeJson = """
                {
                    "name": "Mary",
                    "age": 31,
                    "salary": 5000.0,
                    "gender": "Female"
                }
                """;
        mockMvc.perform(post("/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(femaleEmployeeJson));

        mockMvc.perform(get("/v1/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Smith"))
                .andExpect(jsonPath("$[0].age").value(34))
                .andExpect(jsonPath("$[0].gender").value("Male"))
                .andExpect(jsonPath("$[0].salary").value(5000.0))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Mary"))
                .andExpect(jsonPath("$[1].age").value(31))
                .andExpect(jsonPath("$[1].gender").value("Female"))
                .andExpect(jsonPath("$[1].salary").value(5000.0));
    }

    @Test
    void should_update_age_and_salary_when_update_given_valid_id() throws Exception {
        String requestBody = """
                {
                    "name": "John Smith",
                    "age": 34,
                    "salary": 5000.0,
                    "gender": "Male"
                }
                """;
        mockMvc.perform(post("/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        String updateBody = """
            {
                "name" : "John Smith",
                "age": 30,
                "salary": 10000.0,
                "gender": "Male"
            }
            """;
        mockMvc.perform(put("/v1/employees/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.salary").value(10000.0))
                .andExpect(jsonPath("$.gender").value("Male"));
    }

    @Test
    void should_delete_employee_when_delete_given_valid_id() throws Exception {
        String requestBody = """
                {
                    "name": "Mary",
                    "age": 31,
                    "salary": 5000.0,
                    "gender": "Female"
                }
                """;
        mockMvc.perform(post("/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        mockMvc.perform(delete("/v1/employees/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_return_paginated_result_when_getEmployee_given_page_and_size() throws Exception {
        String requestBody1 = """
                {
                    "name": "John Smith1",
                    "age": 34,
                    "salary": 5000.0,
                    "gender": "Male"
                }
                """;
        mockMvc.perform(post("/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody1));

        String requestBody2 = """
                {
                    "name": "John Smith2",
                    "age": 34,
                    "salary": 5000.0,
                    "gender": "Male"
                }
                """;
        mockMvc.perform(post("/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody2));

        String requestBody3 = """
                {
                    "name": "John Smith3",
                    "age": 34,
                    "salary": 5000.0,
                    "gender": "Male"
                }
                """;
        mockMvc.perform(post("/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody3));

        String requestBody4 = """
                {
                    "name": "John Smith4",
                    "age": 34,
                    "salary": 5000.0,
                    "gender": "Male"
                }
                """;
        mockMvc.perform(post("/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody4));

        String requestBody5 = """
                {
                    "name": "John Smith5",
                    "age": 34,
                    "salary": 5000.0,
                    "gender": "Male"
                }
                """;
        mockMvc.perform(post("/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody5));

        String requestBody6 = """
                {
                    "name": "John Smith6",
                    "age": 34,
                    "salary": 5000.0,
                    "gender": "Male"
                }
                """;
        mockMvc.perform(post("/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody6));

        mockMvc.perform(get("/v1/employees?page=1&size=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].name").value("John Smith2"))
                .andExpect(jsonPath("$[1].name").value("John Smith3"))
                .andExpect(jsonPath("$[2].name").value("John Smith4"))
                .andExpect(jsonPath("$[3].name").value("John Smith5"))
                .andExpect(jsonPath("$[4].name").value("John Smith6"));
    }
}
