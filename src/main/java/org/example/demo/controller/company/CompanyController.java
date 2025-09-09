package org.example.demo.controller.company;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/companies")
public class CompanyController {
    private List<Company> companies = new ArrayList<>();
    private static long idCounter = 0;

    @PostMapping
    public ResponseEntity<Map<String, Long>> createCompany(@RequestBody Company company) {
        company.setId(++idCounter);
        companies.add(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", company.getId()));
    }

    @GetMapping("/{id}")
    public Company getCompanyById(@PathVariable long id) {
        return companies.stream().filter(company -> company.getId() == id).findFirst().orElse(null);
    }
}
