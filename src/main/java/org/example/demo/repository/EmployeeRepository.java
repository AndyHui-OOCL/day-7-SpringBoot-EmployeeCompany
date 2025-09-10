package org.example.demo.repository;

import org.example.demo.controller.employee.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeRepository {
    private final List<Employee> employees = new ArrayList<>();
    private static long idCounter = 0;

    public void insertEmployee(Employee employee) {
        employee.setId(++idCounter);
        employees.add(employee);
    }

    public Employee findEmployeeById(long id) {
        return employees.stream().filter(employee -> employee.getId() == id).findFirst().orElse(null);
    }

    public List<Employee> findEmployeeByGender(String gender) {
        return employees.stream().filter(employee -> employee.getGender().equals(gender)).toList();
    }

    public List<Employee> findAllEmployee() {
        return employees;
    }

    public Employee updateEmployee(Employee targetEmployee, Employee employeeUpdate) {
        targetEmployee.setAge(employeeUpdate.getAge());
        targetEmployee.setSalary(employeeUpdate.getSalary());
        targetEmployee.setName(employeeUpdate.getName());
        targetEmployee.setGender(employeeUpdate.getGender());
        return targetEmployee;
    }

    public void deleteEmployeeById(long id) {
        Employee targetEmployee = findEmployeeById(id);
        employees.remove(targetEmployee);
    }

    public List<Employee> findEmployeeWithPagination(int page, int size) {
        return employees.stream().skip(page).limit(size).toList();
    }
}
