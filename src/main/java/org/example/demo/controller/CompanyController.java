package org.example.demo.controller;

import org.example.demo.Company;
import org.example.demo.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping
    public ResponseEntity<Map<String, Long>> createCompany(@RequestBody Company company) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.createCompany(company));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable long id) {
        Company result = companyService.getCompanyById(id);
        return result != null ? ResponseEntity.status(HttpStatus.OK).body(result) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
     }

    @GetMapping
    public List<Company> getAllCompanies() {return companyService.getAllCompanies();}

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompanyName(@PathVariable long id, @RequestBody Company companyUpdate) {
        Company result = companyService.updateCompanyName(id, companyUpdate);
        return result != null ? ResponseEntity.status(HttpStatus.OK).body(result) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompanyById(@PathVariable long id) {
        companyService.deleteCompanyById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<List<Company>> queryCompaniesWithPagination(@RequestParam int page, @RequestParam int size) {
        List<Company> result = companyService.queryCompaniesWithPagination(page, size);
        return result != null ? ResponseEntity.status(HttpStatus.OK).body(result) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
