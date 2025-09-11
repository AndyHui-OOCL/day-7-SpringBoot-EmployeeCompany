package org.example.demo.service;

import org.example.demo.Employee;
import org.example.demo.exception.EmployeeInactiveException;
import org.example.demo.exception.EmployeeNotFoundException;
import org.example.demo.exception.InvalidEmployeeCreationCriteriaException;
import org.example.demo.exception.InvalidPaginationNumberException;
import org.example.demo.repository.employee.EmployeeRepositoryInMemoryImpl;
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
    private EmployeeRepositoryInMemoryImpl employeeRepository;

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
        verify(employeeRepository, never()).createEmployee(mockEmployee);
    }

    @Test
    void should_throw_error_when_post_given_employee_age_above_65() {
        Employee mockEmployee = new Employee();
        mockEmployee.setName("Tom");
        mockEmployee.setAge(66);
        mockEmployee.setGender("Male");
        mockEmployee.setSalary(1000.0);

        assertThrows(InvalidEmployeeCreationCriteriaException.class, () -> employeeService.createEmployee(mockEmployee));
        verify(employeeRepository, never()).createEmployee(mockEmployee);
    }

    @Test
    void should_throw_error_when_post_given_employee_on_or_above_30_and_salary_below_20000() {
        Employee mockEmployee = new Employee();
        mockEmployee.setName("Tom");
        mockEmployee.setAge(30);
        mockEmployee.setGender("Male");
        mockEmployee.setSalary(5000.0);

        assertThrows(InvalidEmployeeCreationCriteriaException.class, () -> employeeService.createEmployee(mockEmployee));
        verify(employeeRepository, never()).createEmployee(mockEmployee);
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
        verify(employeeRepository, never()).createEmployee(mockEmployee);
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
        verify(employeeRepository, times(1)).createEmployee(mockEmployee);
    }

    @Test
    void should_throw_error_when_get_given_invalid_id() {
        when(employeeRepository.retrieveEmployeeById(anyLong())).thenReturn(null);

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(1));
        verify(employeeRepository, times(1)).retrieveEmployeeById(1);
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

        when(employeeRepository.retrieveEmployeeById(1)).thenReturn(mockEmployee);

        Employee resultEmployee = employeeService.getEmployeeById(1);
        assertEquals(mockEmployee.getId(), resultEmployee.getId());
        assertEquals(mockEmployee.getAge(), resultEmployee.getAge());
        assertEquals(mockEmployee.getSalary(), resultEmployee.getSalary());
        assertEquals(mockEmployee.getGender(), resultEmployee.getGender());
        assertEquals(mockEmployee.getName(), resultEmployee.getName());
        assertEquals(mockEmployee.getStatus(), resultEmployee.getStatus());
        verify(employeeRepository, times(1)).retrieveEmployeeById(1);
    }

    @Test
    void should_set_status_false_when_delete_given_valid_employee_id() {
        Employee mockEmployee = new Employee();
        mockEmployee.setId(1);
        mockEmployee.setName("Tom");
        mockEmployee.setAge(66);
        mockEmployee.setGender("Male");
        mockEmployee.setSalary(10000.0);
        mockEmployee.setStatus(false);

        when(employeeRepository.retrieveEmployeeById(1)).thenReturn(mockEmployee);
        doNothing().when(employeeRepository).deleteEmployeeById(1);

        employeeService.deleteEmployeeById(1);
        assertFalse(mockEmployee.getStatus());
        verify(employeeRepository, times(1)).deleteEmployeeById(1);
    }

    @Test
    void should_throw_exception_when_delete_given_invalid_employee_id() {
        when(employeeRepository.retrieveEmployeeById(1)).thenReturn(null);
        doNothing().when(employeeRepository).deleteEmployeeById(1);

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.deleteEmployeeById(1));
        verify(employeeRepository, times(0)).deleteEmployeeById(1);
    }

    @Test
    void should_update_employee_when_update_given_employee_is_active() {
        Employee mockEmployee = new Employee();
        mockEmployee.setId(1);
        mockEmployee.setName("Tom");
        mockEmployee.setAge(66);
        mockEmployee.setGender("Male");
        mockEmployee.setSalary(20000.0);
        mockEmployee.setStatus(true);

        Employee updatedEmployee = new Employee();
        updatedEmployee.setId(1);
        updatedEmployee.setAge(67);
        updatedEmployee.setName("Tom");
        updatedEmployee.setGender("Male");
        updatedEmployee.setSalary(21000.0);
        updatedEmployee.setStatus(true);

        when(employeeRepository.retrieveEmployeeById(1)).thenReturn(mockEmployee);
        when(employeeRepository.updateEmployee(mockEmployee, updatedEmployee)).thenReturn(updatedEmployee);

        Employee result = employeeService.updateEmployeeInformation(1, updatedEmployee);
        assertEquals(updatedEmployee.getAge(), result.getAge());
        assertEquals(updatedEmployee.getId(), result.getId());
        assertEquals(updatedEmployee.getGender(), result.getGender());
        assertEquals(updatedEmployee.getName(), result.getName());
        assertEquals(updatedEmployee.getId(), result.getId());
        assertTrue(updatedEmployee.getStatus());

        verify(employeeRepository, times(1)).retrieveEmployeeById(1);
        verify(employeeRepository, times(1)).updateEmployee(mockEmployee, updatedEmployee);
    }

    @Test
    void should_throw_error_when_update_given_employee_is_not_active() {
        Employee mockEmployee = new Employee();
        mockEmployee.setId(1);
        mockEmployee.setName("Tom");
        mockEmployee.setAge(66);
        mockEmployee.setGender("Male");
        mockEmployee.setSalary(20000.0);
        mockEmployee.setStatus(false);

        Employee updatedEmployee = new Employee();
        updatedEmployee.setId(1);
        updatedEmployee.setAge(67);
        updatedEmployee.setName("Tom");
        updatedEmployee.setGender("Male");
        updatedEmployee.setSalary(21000.0);
        updatedEmployee.setStatus(true);

        when(employeeRepository.retrieveEmployeeById(1)).thenReturn(mockEmployee);
        when(employeeRepository.updateEmployee(mockEmployee, updatedEmployee)).thenReturn(null);

        assertThrows(EmployeeInactiveException.class, () -> employeeService.updateEmployeeInformation(1, updatedEmployee));

        verify(employeeRepository, times(1)).retrieveEmployeeById(1);
        verify(employeeRepository, times(0)).updateEmployee(mockEmployee, updatedEmployee);
    }

    @Test
    void should_throw_error_when_update_given_employee_does_not_exist() {
        Employee updatedEmployee = new Employee();
        updatedEmployee.setId(1);
        updatedEmployee.setAge(67);
        updatedEmployee.setName("Tom");
        updatedEmployee.setGender("Male");
        updatedEmployee.setSalary(21000.0);
        updatedEmployee.setStatus(true);

        when(employeeRepository.retrieveEmployeeById(1)).thenReturn(null);

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.updateEmployeeInformation(1, updatedEmployee));

        verify(employeeRepository, times(1)).retrieveEmployeeById(1);
        verify(employeeRepository, times(0)).updateEmployee(any(), eq(updatedEmployee));
    }

    @Test
    void should_throw_error_when_query_given_invalid_pagination_number_size() {
        assertThrows(InvalidPaginationNumberException.class, () -> employeeService.queryEmployeesWithPagination(1, 0));
        verify(employeeRepository, times(0)).retrieveAllEmployee();
    }

    @Test
    void should_throw_error_when_query_given_invalid_pagination_number_page() {
        assertThrows(InvalidPaginationNumberException.class, () -> employeeService.queryEmployeesWithPagination(-1, 1));
        verify(employeeRepository, times(0)).retrieveAllEmployee();
    }
}