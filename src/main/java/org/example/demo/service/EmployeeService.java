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
            throw new EmployeeNotWithinLegalAgeException();
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

    public void deleteEmployeeById(long id) {
        employeeRepository.deleteEmployeeById(id);
    }

    public List<Employee> queryEmployeesWithPagination(int page, int size) {
        if(size < 1 || page < 0) {
            return null;
        }
        return employeeRepository.findEmployeeWithPagination(page, size);
    }
}
