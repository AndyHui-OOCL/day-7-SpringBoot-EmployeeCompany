package org.example.demo.service;

import org.example.demo.controller.employee.Employee;
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
        if(employee.getAge() >= 30 && employee.getSalary() < 20000f) {
            throw new InvalidEmployeeCreationCriteriaException("Employee at or over 30 should now have salary below 20000.00");
        }
        employeeRepository.insertEmployee(employee);
        return Map.of("id", employee.getId());
    }

    public Employee getEmployeeById(long id) {
        Employee result = employeeRepository.findEmployeeById(id);
        if(result == null) {
            throw new EmployeeNotFoundException();
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
            return null;
        }
        return employeeRepository.updateEmployee(targetEmployee, employeeUpdate);
    }

    public long deleteEmployeeById(long id) {
        return employeeRepository.deleteEmployeeById(id);
    }

    public List<Employee> queryEmployeesWithPagination(int page, int size) {
        if(size < 1 || page < 0) {
            return null;
        }
        return employeeRepository.findEmployeeWithPagination(page, size);
    }
}
