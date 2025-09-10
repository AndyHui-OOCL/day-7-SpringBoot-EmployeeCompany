package org.example.demo.controller.employee;

import org.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return employeeService.createEmployee(employee);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable long id) {
        return employeeService.getEmployeeById(id);
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
        return employeeService.updateEmployeeAgeAndSalary(id, employeeUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeById(@PathVariable long id) {
        return employeeService.deleteEmployeeById(id);
    }

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<List<Employee>> queryEmployeesWithPagination(@RequestParam int page, @RequestParam int size) {
        return employeeService.queryEmployeesWithPagination(page,size);
    }
}