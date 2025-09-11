package org.example.demo.repository.company;

import org.example.demo.Company;

import java.util.List;

public interface CompanyRepository {
    void createCompany(Company company);

    Company retrieveCompanyById(long id);

    List<Company> retrieveAllCompanies();

    Company updateCompany(Company targetCompany, Company companyUpdate);

    Company deleteCompanyById(long id);

    List<Company> findCompaniesWithPagination(int page, int size);
}
