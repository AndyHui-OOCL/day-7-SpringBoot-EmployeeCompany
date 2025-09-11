package org.example.demo.repository.company;

import org.example.demo.Company;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CompanyRepositoryMemoryImpl implements CompanyRepository {
    private final List<Company> companies = new ArrayList<>();
    private static long idCounter = 0;


    public void createCompany(Company company) {
        company.setId(++idCounter);
        companies.add(company);
    }

    public Company retrieveCompanyById(long id) {
        return companies.stream().filter(company -> company.getId() == id).findFirst().orElse(null);
    }

    public List<Company> retrieveAllCompanies() {
        return companies;
    }

    public Company updateCompany(Company targetCompany, Company companyUpdate) {
        targetCompany.setName(companyUpdate.getName());
        return targetCompany;
    }

    public Company deleteCompanyById(long id) {
        Company targetCompany = retrieveCompanyById(id);
        if (targetCompany == null) {
            return null;
        }
        companies.remove(targetCompany);
        return targetCompany;
    }

    public List<Company> findCompaniesWithPagination(int page, int size) {
        return companies.subList((page - 1) * size, page * size);
    }

    public void cleanup() {
        companies.clear();
        idCounter = 0;
    }
}
