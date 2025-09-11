package org.example.demo.repository.employee;

import org.example.demo.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeRepositoryInMemoryImpl implements EmployeeRepository {
    private final List<Employee> employees = new ArrayList<>();
    private static long idCounter = 0;

    @Override
    public long createEmployee(Employee employee) {
        employee.setId(++idCounter);
        employee.setStatus(true);
        employees.add(employee);
        return employee.getId();
    }

    public Employee retrieveEmployeeById(long id) {
        return employees.stream().filter(employee -> employee.getId() == id).findFirst().orElse(null);
    }

    public List<Employee> retrieveEmployeeByGender(String gender) {
        return employees.stream().filter(employee -> employee.getGender().equals(gender)).toList();
    }

    public List<Employee> retrieveAllEmployee() {
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
        Employee targetEmployee = retrieveEmployeeById(id);
        targetEmployee.setStatus(false);
    }

    public List<Employee> retrieveEmployeeWithPagination(int page, int size) {
        return employees.subList((page - 1) * size, page * size);
    }

    public boolean hasDuplicateEmployee(Employee newEmployee) {
        return !employees.stream()
                .filter(employee -> employee.getName().equals(newEmployee.getName()) && employee.getGender().equals(newEmployee.getGender()))
                .toList()
                .isEmpty();
    }

    public void cleanUp() {
        employees.clear();
        idCounter = 0;
    }
}
