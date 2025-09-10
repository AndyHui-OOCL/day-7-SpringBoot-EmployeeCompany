package org.example.demo.service;

import org.example.demo.controller.employee.Employee;
import org.example.demo.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void should_not_create_employee_when_post_given_employee_age_below_18() {
        Employee mockEmployee = new Employee();
        mockEmployee.setName("Tom");
        mockEmployee.setAge(12);
        mockEmployee.setGender("Male");
        mockEmployee.setSalary(1000.0);

        assertThrows(EmploeeNotWithinLegalAgeException.class, () -> employeeService.createEmployee(mockEmployee));
    }

    @Test
    void should_not_create_employee_when_post_given_employee_age_above_65() {
        Employee mockEmployee = new Employee();
        mockEmployee.setName("Tom");
        mockEmployee.setAge(65);
        mockEmployee.setGender("Male");
        mockEmployee.setSalary(1000.0);

        assertThrows(EmploeeNotWithinLegalAgeException.class, () -> employeeService.createEmployee(mockEmployee));
    }

}