package org.example.demo;

import org.example.demo.controller.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

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
        mockMvc.perform(get("/v1/employees?gender=Female")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void should_get_all_employee_when_getAll_given_2_employee() throws Exception {
        String requestBody = """
                {
                    "name": "Mary",
                    "age": 31,
                    "salary": 5000.0,
                    "gender": "Feale"
                }
                """;
        mockMvc.perform(post("/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        mockMvc.perform(get("/v1/employees/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }
}
