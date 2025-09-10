package org.example.demo.controller.company;

import org.example.demo.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return companyService.createCompany(company);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable long id) {
        return companyService.getCompanyById(id);
     }

    @GetMapping
    public List<Company> getAllCompanies() {return companyService.getAllCompanies();}

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompanyName(@PathVariable long id, @RequestBody Company companyUpdate) {
        return companyService.updateCompanyName(id, companyUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompanyById(@PathVariable long id) {
        return companyService.deleteCompanyById(id);
    }

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<List<Company>> queryCompaniesWithPagination(@RequestParam int page, @RequestParam int size) {
        return companyService.queryCompaniesWithPagination(page, size);
    }
}
