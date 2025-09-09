package org.example.demo.controller.employee;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/employees")
public class EmployeeController {
    private List<Employee> employees = new ArrayList<>();
    private static long idCounter = 0;

    @PostMapping
    public ResponseEntity<Map<String, Long>> createEmployee(@RequestBody Employee employee) {
        employee.setId(++idCounter);
        employees.add(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", employee.getId()));
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable long id) {
        return employees.stream().filter(employee -> employee.getId() == id).findFirst().orElse(null);
    }

    @GetMapping(params = "gender")
    public List<Employee> queryEmployeeByGender(@RequestParam String gender) {
        return employees.stream().filter(employee -> employee.getGender().equals(gender)).toList();
    }

    @GetMapping()
    public List<Employee> getEmployeeList() {
        return employees;
    }

    @PutMapping("/{id}")
    public Employee updateEmployeeAgeAndSalary(@PathVariable long id, @RequestBody Employee employeeUpdate) {
        Employee targetEmployee = employees.stream().filter(employee -> employee.getId() == id).findFirst().orElse(null);;
        targetEmployee.setAge(employeeUpdate.getAge());
        targetEmployee.setSalary(employeeUpdate.getSalary());
        return targetEmployee;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Employee> deleteEmployeeById(@PathVariable long id) {
        Employee targetEmployee = employees.stream().filter(employee -> employee.getId() == id).findFirst().orElse(null);
        employees.remove(targetEmployee);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping(params = {"page", "size"})
    public List<Employee> queryEmployeesWithPagination(@RequestParam int page, @RequestParam int size) {
        return employees.stream().skip(page).limit(size).toList();
    }
}