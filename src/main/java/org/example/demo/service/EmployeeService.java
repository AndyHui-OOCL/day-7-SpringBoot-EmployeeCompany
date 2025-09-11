package org.example.demo.service;

import org.example.demo.Employee;
import org.example.demo.controller.request.UpdateEmployeeRequest;
import org.example.demo.service.exception.EmployeeInactiveException;
import org.example.demo.service.exception.EmployeeNotFoundException;
import org.example.demo.service.exception.InvalidEmployeeCreationCriteriaException;
import org.example.demo.service.exception.InvalidPaginationNumberException;
import org.example.demo.repository.employee.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public Map<String, Long> createEmployee(Employee employee) {
        if (employee.getAge() < 18 || employee.getAge() > 65) {
            throw new InvalidEmployeeCreationCriteriaException("Employee age should be within 18 - 65");
        }
        if (employee.getAge() >= 30 && employee.getSalary() < 20000) {
            throw new InvalidEmployeeCreationCriteriaException("Employee at or over 30 should now have salary below 20000.00");
        }
        if (employeeRepository.hasDuplicateEmployee(employee)) {
            throw new InvalidEmployeeCreationCriteriaException("Employee with same name and gender already exists");
        }
        return Map.of("id", employeeRepository.createEmployee(employee));
    }

    public Employee getEmployeeById(long id) {
        Employee retrievedEmployee = employeeRepository.retrieveEmployeeById(id);
        if (retrievedEmployee == null) {
            throw new EmployeeNotFoundException(String.format("Employee with id %d is not found", id));
        }
        return retrievedEmployee;
    }

    public List<Employee> queryEmployeeByGender(@RequestParam String gender) {
        return employeeRepository.retrieveEmployeeByGender(gender);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.retrieveAllEmployee();
    }

    public Employee updateEmployeeInformation(long id, UpdateEmployeeRequest updateEmployeeRequest) {
        Employee targetEmployee = getEmployeeById(id);
        if (!targetEmployee.getStatus()) {
            throw new EmployeeInactiveException("The specified employee is inactive");
        }
        targetEmployee.setSalary(updateEmployeeRequest.getSalary());
        targetEmployee.setAge(updateEmployeeRequest.getAge());
        targetEmployee.setGender(updateEmployeeRequest.getGender());
        targetEmployee.setName(updateEmployeeRequest.getName());
        return employeeRepository.updateEmployee(targetEmployee);
    }

    public void deleteEmployeeById(long id) {
        getEmployeeById(id);
        employeeRepository.deleteEmployeeById(id);
    }

    public List<Employee> queryEmployeesWithPagination(int page, int size) {
        if (size < 1 || page < 0) {
            throw new InvalidPaginationNumberException("Page number should be large than 0 and size should be larger than 1");
        }
        return employeeRepository.retrieveEmployeeWithPagination(page, size);
    }
}
