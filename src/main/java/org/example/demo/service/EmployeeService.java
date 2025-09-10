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

    public Map<String, Long> createEmployee(Employee employee) {
        employee.setId(++idCounter);
        employees.add(employee);
        return Map.of("id", employee.getId());
    }

    public Employee getEmployeeById(long id) {
        return employees.stream().filter(employee -> employee.getId() == id).findFirst().orElse(null);
    }

    public List<Employee> queryEmployeeByGender(@RequestParam String gender) {
        return employees.stream().filter(employee -> employee.getGender().equals(gender)).toList();
    }

    public List<Employee> getAllEmployees() {
        return employees;
    }

    public Employee updateEmployeeAgeAndSalary(long id, Employee employeeUpdate) {
        Employee targetEmployee = employees.stream().filter(employee -> employee.getId() == id).findFirst().orElse(null);
        if(targetEmployee == null) {
            return null;
        }
        targetEmployee.setAge(employeeUpdate.getAge());
        targetEmployee.setSalary(employeeUpdate.getSalary());
        targetEmployee.setName(employeeUpdate.getName());
        targetEmployee.setGender(employeeUpdate.getGender());
        return targetEmployee;
    }

    public void deleteEmployeeById(long id) {
        Employee targetEmployee = employees.stream().filter(employee -> employee.getId() == id).findFirst().orElse(null);
        employees.remove(targetEmployee);
    }

    public List<Employee> queryEmployeesWithPagination(int page, int size) {
        if(size < 1 || page < 0) {
            return null;
        }
        return employees.stream().skip(page).limit(size).toList();
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void clearEmployees() {
        employees.clear();
    }

    public void resetIdCounter() {
        idCounter = 0;
    }
}
