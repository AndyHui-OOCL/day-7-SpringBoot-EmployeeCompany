package org.example.demo.service;

import org.example.demo.controller.employee.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {
    private final List<Employee> employees = new ArrayList<>();
    private static long idCounter = 0;

    public ResponseEntity<Map<String, Long>> createEmployee(Employee employee) {
        employee.setId(++idCounter);
        employees.add(employee);
        return  ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", employee.getId()));
    }

    public ResponseEntity<Employee> getEmployeeById(long id) {
        Employee targetEmployee =  employees.stream().filter(employee -> employee.getId() == id).findFirst().orElse(null);
        if(targetEmployee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(targetEmployee);
    }

    public List<Employee> queryEmployeeByGender(@RequestParam String gender) {
        return employees.stream().filter(employee -> employee.getGender().equals(gender)).toList();
    }

    public List<Employee> getAllEmployees() {
        return employees;
    }

    public ResponseEntity<Employee> updateEmployeeAgeAndSalary(long id, Employee employeeUpdate) {
        Employee targetEmployee = employees.stream().filter(employee -> employee.getId() == id).findFirst().orElse(null);
        if(targetEmployee == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        targetEmployee.setAge(employeeUpdate.getAge());
        targetEmployee.setSalary(employeeUpdate.getSalary());
        targetEmployee.setName(employeeUpdate.getName());
        targetEmployee.setGender(employeeUpdate.getGender());
        return ResponseEntity.status(HttpStatus.OK).body(targetEmployee);
    }

    public ResponseEntity<Void> deleteEmployeeById(long id) {
        Employee targetEmployee = employees.stream().filter(employee -> employee.getId() == id).findFirst().orElse(null);
        employees.remove(targetEmployee);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    public ResponseEntity<List<Employee>> queryEmployeesWithPagination(int page, int size) {
        if(size < 1 || page < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(employees.stream().skip(page).limit(size).toList());
    }
}
