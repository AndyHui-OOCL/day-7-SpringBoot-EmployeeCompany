package org.example.demo.repository;

import org.example.demo.Employee;

import java.util.List;

public interface EmployeeRepository {
    void createEmployee(Employee employee);

    Employee retrieveEmployeeById(long id);

    List<Employee> retrieveEmployeeByGender(String gender);

    List<Employee> retrieveAllEmployee()''

    Employee updateEmployee(Employee targetEmployee, Employee employeeUpdate);

    Employee deleteEmployeeById(long id);

    List<Employee> retrieveEmployeeWithPagination(int page, int size);

    boolean hasDuplicateEmployee(Employee newEmployee);
}
