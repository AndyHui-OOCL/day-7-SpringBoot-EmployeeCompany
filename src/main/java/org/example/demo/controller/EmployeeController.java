package org.example.demo.controller;

import org.example.demo.Employee;
import org.example.demo.controller.request.UpdateEmployeeRequest;
import org.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Map<String, Long>> createEmployee(@RequestBody Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(employee));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
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
    public ResponseEntity<Employee> updateEmployeeInformation(@PathVariable long id, @RequestBody UpdateEmployeeRequest updateEmployeeRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.updateEmployeeInformation(id, updateEmployeeRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeById(@PathVariable long id) {
        employeeService.deleteEmployeeById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<List<Employee>> queryEmployeesWithPagination(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.queryEmployeesWithPagination(page, size));
    }
}