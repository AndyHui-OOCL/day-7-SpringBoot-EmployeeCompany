package org.example.demo.service;

import org.example.demo.controller.company.Company;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CompanyService {
    private final List<Company> companies = new ArrayList<>();
    private static long idCounter = 0;

    public ResponseEntity<Map<String, Long>> createCompany(Company company) {
        company.setId(++idCounter);
        companies.add(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", company.getId()));
    }

    public ResponseEntity<Company> getCompanyById(long id) {
        Company targetCompany =  companies.stream().filter(company -> company.getId() == id).findFirst().orElse(null);
        if(targetCompany == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(targetCompany);
    }

    public List<Company> getAllCompanies() {
        return companies;
    }

    public ResponseEntity<Company> updateCompanyName(long id, Company companyUpdate) {
        Company targetCompany = companies.stream().filter(company -> company.getId() == id).findFirst().orElse(null);
        if(targetCompany == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        targetCompany.setName(companyUpdate.getName());
        return ResponseEntity.status(HttpStatus.OK).body(targetCompany);
    }

    public ResponseEntity<Company> deleteCompanyById(long id) {
        Company targetCompany = companies.stream().filter(company -> company.getId() == id).findFirst().orElse(null);
        if(targetCompany == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        companies.remove(targetCompany);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    public ResponseEntity<List<Company>> queryCompaniesWithPagination(int page, int size) {
        if(size < 1 || page < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(companies.stream().skip(page).limit(size).toList());
    }
}
