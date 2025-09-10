package org.example.demo.service;

import org.example.demo.Employee;
import org.example.demo.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void should_throw_error_when_post_given_employee_age_below_18() {
        Employee mockEmployee = new Employee();
        mockEmployee.setName("Tom");
        mockEmployee.setAge(12);
        mockEmployee.setGender("Male");
        mockEmployee.setSalary(1000.0);

        assertThrows(InvalidEmployeeCreationCriteriaException.class, () -> employeeService.createEmployee(mockEmployee));
        verify(employeeRepository, never()).insertEmployee(mockEmployee);
    }

    @Test
    void should_throw_error_when_post_given_employee_age_above_65() {
        Employee mockEmployee = new Employee();
        mockEmployee.setName("Tom");
        mockEmployee.setAge(66);
        mockEmployee.setGender("Male");
        mockEmployee.setSalary(1000.0);

        assertThrows(InvalidEmployeeCreationCriteriaException.class, () -> employeeService.createEmployee(mockEmployee));
        verify(employeeRepository, never()).insertEmployee(mockEmployee);
    }

    @Test
    void should_throw_error_when_post_given_employee_on_or_above_30_and_salary_below_30 () {
        Employee mockEmployee = new Employee();
        mockEmployee.setName("Tom");
        mockEmployee.setAge(30);
        mockEmployee.setGender("Male");
        mockEmployee.setSalary(5000.0);

        assertThrows(InvalidEmployeeCreationCriteriaException.class, () -> employeeService.createEmployee(mockEmployee));
        verify(employeeRepository, never()).insertEmployee(mockEmployee);
    }

    @Test
    void should_throw_error_when_post_given_duplicated_employee() {
        Employee mockEmployee = new Employee();
        mockEmployee.setId(1);
        mockEmployee.setName("Tom");
        mockEmployee.setAge(30);
        mockEmployee.setGender("Male");
        mockEmployee.setSalary(25000.0);
        mockEmployee.setStatus(true);

        when(employeeRepository.hasDuplicateEmployee(mockEmployee)).thenReturn(true);

        assertThrows(InvalidEmployeeCreationCriteriaException.class, () -> employeeService.createEmployee(mockEmployee));
        verify(employeeRepository, never()).insertEmployee(mockEmployee);
    }

    @Test
    void should_create_employee_with_status_true_when_post_given_valid_creation_criteria() {
        Employee mockEmployee = new Employee();
        mockEmployee.setId(1);
        mockEmployee.setName("Tom");
        mockEmployee.setAge(30);
        mockEmployee.setGender("Male");
        mockEmployee.setSalary(52000.0);
        mockEmployee.setStatus(true);

        Map<String, Long> result = employeeService.createEmployee(mockEmployee);
        assertEquals(1, result.get("id"));
        assertTrue(mockEmployee.getStatus());
        verify(employeeRepository, times(1)).insertEmployee(mockEmployee);
    }

    @Test
    void should_throw_error_when_get_given_invalid_id() {
        when(employeeRepository.findEmployeeById(anyLong())).thenReturn(null);

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(1));
        verify(employeeRepository, times(1)).findEmployeeById(1);
    }

    @Test
    void should_get_employee_when_get_given_valid_id() {
        Employee mockEmployee = new Employee();
        mockEmployee.setId(1);
        mockEmployee.setName("Tom");
        mockEmployee.setAge(66);
        mockEmployee.setGender("Male");
        mockEmployee.setSalary(1000.0);
        mockEmployee.setStatus(true);

        when(employeeRepository.findEmployeeById(1)).thenReturn(mockEmployee);

        Employee resultEmployee = employeeService.getEmployeeById(1);
        assertEquals(mockEmployee.getId(), resultEmployee.getId());
        assertEquals(mockEmployee.getAge(), resultEmployee.getAge());
        assertEquals(mockEmployee.getSalary(), resultEmployee.getSalary());
        assertEquals(mockEmployee.getGender(), resultEmployee.getGender());
        assertEquals(mockEmployee.getName(), resultEmployee.getName());
        assertEquals(mockEmployee.getStatus(), resultEmployee.getStatus());
        verify(employeeRepository, times(1)).findEmployeeById(1);
    }

    @Test
    void should_delete_employee_when_delete_given_valid_employee_id() {
        Employee mockEmployee = new Employee();
        mockEmployee.setId(1);
        mockEmployee.setName("Tom");
        mockEmployee.setAge(66);
        mockEmployee.setGender("Male");
        mockEmployee.setSalary(1000.0);
        mockEmployee.setStatus(false);

        when(employeeRepository.deleteEmployeeById(1)).thenReturn(mockEmployee);

        Employee result = employeeService.deleteEmployeeById(1);
        assertEquals(mockEmployee.getId(), result.getId());
        assertEquals(mockEmployee.getName(), result.getName());
        assertEquals(mockEmployee.getSalary(), result.getSalary());
        assertEquals(mockEmployee.getGender(), result.getGender());
        assertEquals(mockEmployee.getAge(), result.getAge());
        assertFalse(mockEmployee.getStatus());
        verify(employeeRepository, times(1)).deleteEmployeeById(1);
    }
}