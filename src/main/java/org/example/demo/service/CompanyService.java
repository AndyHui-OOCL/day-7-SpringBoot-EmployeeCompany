package org.example.demo.service;

import org.example.demo.controller.company.Company;
import org.example.demo.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public ResponseEntity<Map<String, Long>> createCompany(Company company) {
        companyRepository.insertCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", company.getId()));
    }

    public ResponseEntity<Company> getCompanyById(long id) {
        Company targetCompany = companyRepository.findCompanyById(id);
        if(targetCompany == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(targetCompany);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAllCompanies();
    }

    public ResponseEntity<Company> updateCompanyName(long id, Company companyUpdate) {
        Company targetCompany = companyRepository.findCompanyById(id);
        if(targetCompany == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Company result = companyRepository.updateCompany(targetCompany, companyUpdate);;
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public ResponseEntity<Void> deleteCompanyById(long id) {
        companyRepository.deleteCompanyById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    public ResponseEntity<List<Company>> queryCompaniesWithPagination(int page, int size) {
        if(size < 1 || page < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(companyRepository.findCompaniesWithPagination(page, size));
    }
}
