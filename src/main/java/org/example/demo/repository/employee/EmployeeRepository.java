package org.example.demo.repository.employee;

import org.example.demo.Employee;
import org.example.demo.controller.UpdateEmployeeReq;

import java.util.List;

public interface EmployeeRepository {
    void cleanUp();

    long createEmployee(Employee employee);

    Employee retrieveEmployeeById(long id);

    List<Employee> retrieveEmployeeByGender(String gender);

    List<Employee> retrieveAllEmployee();

    Employee updateEmployee(Employee updateEmployee);

    void deleteEmployeeById(long id);

    List<Employee> retrieveEmployeeWithPagination(int page, int size);

    boolean hasDuplicateEmployee(Employee newEmployee);
}
