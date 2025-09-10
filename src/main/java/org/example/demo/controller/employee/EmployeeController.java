package org.example.demo.controller.employee;

import org.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Map<String, Long>> createEmployee(@RequestBody Employee employee) {
        Map<String, Long> result = employeeService.createEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable long id) {
        Employee targetEmployee = employeeService.getEmployeeById(id);
        return targetEmployee != null ? ResponseEntity.status(HttpStatus.OK).body(targetEmployee) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping(params = "gender")
    public List<Employee> queryEmployeeByGender(@RequestParam String gender) {
        return employeeService.queryEmployeeByGender(gender);
    }

    @GetMapping()
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployeeAgeAndSalary(@PathVariable long id, @RequestBody Employee employeeUpdate) {
        Employee result = employeeService.updateEmployeeAgeAndSalary(id, employeeUpdate);
        return result != null ? ResponseEntity.status(HttpStatus.OK).body(result) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeById(@PathVariable long id) {
        employeeService.deleteEmployeeById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<List<Employee>> queryEmployeesWithPagination(@RequestParam int page, @RequestParam int size) {
        List<Employee> result = employeeService.queryEmployeesWithPagination(page,size);
        return result != null ? ResponseEntity.status(HttpStatus.OK).body(result) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}