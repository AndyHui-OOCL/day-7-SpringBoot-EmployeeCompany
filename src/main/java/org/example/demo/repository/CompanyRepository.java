package org.example.demo.repository;

import org.example.demo.Company;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CompanyRepository {
    private final List<Company> companies = new ArrayList<>();
    private static long idCounter = 0;


    public void insertCompany(Company company) {
        company.setId(++idCounter);
        companies.add(company);
    }

    public Company findCompanyById(long id) {
        return companies.stream().filter(company -> company.getId() == id).findFirst().orElse(null);
    }

    public List<Company> findAllCompanies() {
        return companies;
    }

    public Company updateCompany(Company targetCompany, Company companyUpdate) {
        targetCompany.setName(companyUpdate.getName());
        return targetCompany;
    }

    public void deleteCompanyById(long id) {
        Company targetCompany = findCompanyById(id);
        companies.remove(targetCompany);
    }

    public List<Company> findCompaniesWithPagination(int page, int size) {
        return companies.stream().skip(page).limit(size).toList();
    }
}
