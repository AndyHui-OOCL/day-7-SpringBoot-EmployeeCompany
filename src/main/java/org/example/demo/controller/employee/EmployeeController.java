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
    private final List<Employee> employees = new ArrayList<>();
    private static long idCounter = 0;

    @PostMapping
    public ResponseEntity<Map<String, Long>> createEmployee(@RequestBody Employee employee) {
        employee.setId(++idCounter);
        employees.add(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", employee.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable long id) {
        Employee targetEmployee = employees.stream().filter(employee -> employee.getId() == id).findFirst().orElse(null);
        return targetEmployee != null ? ResponseEntity.status(HttpStatus.OK).body(targetEmployee) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping(params = "gender")
    public List<Employee> queryEmployeeByGender(@RequestParam String gender) {
        return employees.stream().filter(employee -> employee.getGender().equals(gender)).toList();
    }

    @GetMapping()
    public List<Employee> getAllEmployees() {
        return employees;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployeeAgeAndSalary(@PathVariable long id, @RequestBody Employee employeeUpdate) {
        Employee targetEmployee = employees.stream().filter(employee -> employee.getId() == id).findFirst().orElse(null);
        if(targetEmployee == null) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        targetEmployee.setAge(employeeUpdate.getAge());
        targetEmployee.setSalary(employeeUpdate.getSalary());
        targetEmployee.setName(employeeUpdate.getName());
        targetEmployee.setGender(employeeUpdate.getGender());
        return ResponseEntity.status(HttpStatus.OK).body(targetEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeById(@PathVariable long id) {
        Employee targetEmployee = employees.stream().filter(employee -> employee.getId() == id).findFirst().orElse(null);
        if(targetEmployee == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        employees.remove(targetEmployee);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<List<Employee>> queryEmployeesWithPagination(@RequestParam int page, @RequestParam int size) {
        if(size < 1 || page < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(employees.stream().skip(page).limit(size).toList());
    }
}