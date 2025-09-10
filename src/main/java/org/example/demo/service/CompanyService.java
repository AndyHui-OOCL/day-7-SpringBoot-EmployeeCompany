package org.example.demo.service;

import org.example.demo.Company;
import org.example.demo.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public Map<String, Long> createCompany(Company company) {
        companyRepository.createCompany(company);
        return Map.of("id", company.getId());
    }

    public Company getCompanyById(long id) {
        Company retrievedComapny = companyRepository.retrieveCompanyById(id);
        if(retrievedComapny == null) {
            throw new CompanyNotFoundException(String.format("Company with id %d is not found",id));
        }
        return retrievedComapny;
    }

    public List<Company> getAllCompanies() {
        return companyRepository.retrieveAllCompanies();
    }

    public Company updateCompanyName(long id, Company companyUpdate) {
        Company targetCompany = companyRepository.retrieveCompanyById(id);
        if(targetCompany == null) {
            throw new CompanyNotFoundException(String.format("Company with id %d is not found",id));
        }
        return companyRepository.updateCompany(targetCompany, companyUpdate);
    }

    public void deleteCompanyById(long id) {
        Company companyDeleted = companyRepository.deleteCompanyById(id);
        if (companyDeleted == null) {
            throw new CompanyNotFoundException(String.format("Company with id %d is not found",id));
        }
    }

    public List<Company> queryCompaniesWithPagination(int page, int size) {
        if(size < 1 || page < 0) {
            throw new InvalidPaginationNumberException("Page number should be large than 0 and size should be larger than 1");
        }
        return companyRepository.findCompaniesWithPagination(page, size);
    }
}
