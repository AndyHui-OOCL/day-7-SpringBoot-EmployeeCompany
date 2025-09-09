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
    private final List<Company> companies = new ArrayList<>();
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

    @GetMapping
    public List<Company> getAllCompanies() {return companies;}

    @PutMapping("/{id}")
    public Company updateCompanyName(@PathVariable long id, @RequestBody Company companyUpdate) {
        Company targetCompany = companies.stream().filter(company -> company.getId() == id).findFirst().orElse(null);
        targetCompany.setName(companyUpdate.getName());
        return targetCompany;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Company> deleteCompanyById(@PathVariable long id) {
        Company targetCompany = companies.stream().filter(company -> company.getId() == id).findFirst().orElse(null);
        companies.remove(targetCompany);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping(params = {"page", "size"})
    public List<Company> queryCompaniesWithPagination(@RequestParam int page, @RequestParam int size) {
        return companies.stream().skip(page).limit(size).toList();
    }
}
