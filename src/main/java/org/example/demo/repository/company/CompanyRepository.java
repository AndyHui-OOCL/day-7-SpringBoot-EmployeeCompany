package org.example.demo.repository.company;

import org.example.demo.Company;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository {
    void createCompany(Company company);

    Company retrieveCompanyById(long id);

    List<Company> retrieveAllCompanies();

    Company updateCompany(Company targetCompany, Company companyUpdate);

    void deleteCompanyById(long id);

    List<Company> findCompaniesWithPagination(int page, int size);

    void cleanUp();
}
