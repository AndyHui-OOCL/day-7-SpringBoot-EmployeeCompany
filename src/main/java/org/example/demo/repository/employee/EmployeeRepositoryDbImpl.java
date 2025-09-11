package org.example.demo.repository.employee;

import org.example.demo.Employee;
import org.example.demo.repository.dao.EmployeeJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeRepositoryDbImpl implements EmployeeRepository {

    @Autowired
    private EmployeeJpaRepository repository;

    @Override
    public void cleanUp() {
        repository.deleteAll();
    }

    @Override
    public long createEmployee(Employee employee) {
        return repository.save(employee).getId();
    }

    @Override
    public Employee retrieveEmployeeById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Employee> retrieveEmployeeByGender(String gender) {
        return repository.findByGender(gender);
    }

    @Override
    public List<Employee> retrieveAllEmployee() {
        return repository.findAll();
    }

    @Override
    public Employee updateEmployee(Employee targetEmployee, Employee employeeUpdate) {
        return repository.save(employeeUpdate);
    }

    @Override
    public void deleteEmployeeById(long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Employee> retrieveEmployeeWithPagination(int page, int size) {
        return repository.findAll(PageRequest.of(page - 1, size)).stream().toList();
    }

    @Override
    public boolean hasDuplicateEmployee(Employee newEmployee) {
        List<Employee> employeesWithSameGender = retrieveEmployeeByGender(newEmployee.getGender());
        List<Employee> employeesWithSameName = repository.findByName(newEmployee.getName());
        return !employeesWithSameGender.stream().filter(employeesWithSameName::contains).toList().isEmpty();
    }

    private List<Employee> findEmployeeWithSameGender(String gender) {
        return repository.findByGender(gender).stream().toList();
    }
}
