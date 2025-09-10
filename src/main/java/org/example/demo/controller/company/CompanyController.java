package org.example.demo.controller.company;

import org.apache.coyote.Response;
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
    public ResponseEntity<Company> getCompanyById(@PathVariable long id) {
        Company targetCompany = companies.stream().filter(company -> company.getId() == id).findFirst().orElse(null);
        return targetCompany != null ? ResponseEntity.status(HttpStatus.OK).body(targetCompany) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
     }

    @GetMapping
    public List<Company> getAllCompanies() {return companies;}

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompanyName(@PathVariable long id, @RequestBody Company companyUpdate) {
        Company targetCompany = companies.stream().filter(company -> company.getId() == id).findFirst().orElse(null);
        if(targetCompany == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        targetCompany.setName(companyUpdate.getName());
        return ResponseEntity.status(HttpStatus.OK).body(targetCompany);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Company> deleteCompanyById(@PathVariable long id) {
        Company targetCompany = companies.stream().filter(company -> company.getId() == id).findFirst().orElse(null);
        if(targetCompany == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        companies.remove(targetCompany);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<List<Company>> queryCompaniesWithPagination(@RequestParam int page, @RequestParam int size) {
        if(size < 1 || page < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(companies.stream().skip(page).limit(size).toList());
    }
}
