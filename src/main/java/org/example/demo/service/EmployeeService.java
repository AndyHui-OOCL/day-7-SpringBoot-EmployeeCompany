package org.example.demo.service;

import org.example.demo.Employee;
import org.example.demo.repository.EmployeeRepository;
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
        if(employee.getAge() < 18 || employee.getAge() > 65) {
            throw new InvalidEmployeeCreationCriteriaException("Employee age should be within 18 - 65");
        }
        if(employee.getAge() >= 30 && employee.getSalary() < 20000) {
            throw new InvalidEmployeeCreationCriteriaException("Employee at or over 30 should now have salary below 20000.00");
        }
        if(employeeRepository.hasDuplicateEmployee(employee)) {
            throw new InvalidEmployeeCreationCriteriaException("Employee with same name and gender already exists");
        }
        employeeRepository.insertEmployee(employee);
        return Map.of("id", employee.getId());
    }

    public Employee getEmployeeById(long id) {
        Employee result = employeeRepository.findEmployeeById(id);
        if(result == null) {
            throw new EmployeeNotFoundException(String.format("Employee with id %d is not found", id));
        }
        return result;
    }

    public List<Employee> queryEmployeeByGender(@RequestParam String gender) {
        return employeeRepository.findEmployeeByGender(gender);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAllEmployee();
    }

    public Employee updateEmployeeInformation(long id, Employee employeeUpdate) {
        Employee targetEmployee = employeeRepository.findEmployeeById(id);
        if(targetEmployee == null) {
            throw new EmployeeNotFoundException(String.format("Employee with id %d is not found", id));
        }
        if(!targetEmployee.getStatus()) {
            throw new EmployeeInactiveException("The specified employee is inactive");
        }
        return employeeRepository.updateEmployee(targetEmployee, employeeUpdate);
    }

    public Employee deleteEmployeeById(long id) {
        Employee result = employeeRepository.deleteEmployeeById(id);
        if (result == null) {
            throw new EmployeeNotFoundException(String.format("Employee with id: %d is not found", id));
        }
        return result;
    }

    public List<Employee> queryEmployeesWithPagination(int page, int size){
        if(size < 1 || page < 0) {
            throw new InvalidPaginationNumberException("Page number should be large than 0 and size should be larger than 1");
        }
        return employeeRepository.findEmployeeWithPagination(page, size);
    }
}
