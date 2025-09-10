package org.example.demo.service;

import org.example.demo.controller.employee.Employee;
import org.example.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public ResponseEntity<Map<String, Long>> createEmployee(Employee employee) {
        employeeRepository.insertEmployee(employee);
        return  ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", employee.getId()));
    }

    public ResponseEntity<Employee> getEmployeeById(long id) {
        Employee targetEmployee = employeeRepository.findEmployeeById(id);
        if(targetEmployee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(targetEmployee);
    }

    public List<Employee> queryEmployeeByGender(@RequestParam String gender) {
        return employeeRepository.findEmployeeByGender(gender);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAllEmployee();
    }

    public ResponseEntity<Employee> updateEmployeeAgeAndSalary(long id, Employee employeeUpdate) {
        Employee targetEmployee = employeeRepository.findEmployeeById(id);
        if(targetEmployee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Employee updateResult = employeeRepository.updateEmployee(targetEmployee, employeeUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(updateResult);
    }

    public ResponseEntity<Void> deleteEmployeeById(long id) {
        employeeRepository.deleteEmployeeById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    public ResponseEntity<List<Employee>> queryEmployeesWithPagination(int page, int size) {
        if(size < 1 || page < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(employeeRepository.findEmployeeWithPagination(page, size));
    }
}
