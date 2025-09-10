package org.example.demo.repository;

import org.example.demo.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeRepository {
    private final List<Employee> employees = new ArrayList<>();
    private static long idCounter = 0;

    public void insertEmployee(Employee employee) {
        employee.setId(++idCounter);
        employee.setStatus(true);
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

    public Long deleteEmployeeById(long id) {
        Employee targetEmployee = findEmployeeById(id);
        if(targetEmployee == null) {
            return null;
        }
        targetEmployee.setStatus(false);
        return id;
    }

    public List<Employee> findEmployeeWithPagination(int page, int size) {
        return employees.stream().skip(page).limit(size).toList();
    }

    public boolean hasDuplicateEmployee(Employee newEmployee) {
        return !employees.stream()
                .filter(employee -> employee.getName().equals(newEmployee.getName()) && employee.getGender().equals(newEmployee.getGender()))
                .toList()
                .isEmpty();
    }
}
