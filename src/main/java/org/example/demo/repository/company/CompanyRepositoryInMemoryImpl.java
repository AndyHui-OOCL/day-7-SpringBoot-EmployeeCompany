package org.example.demo.repository.company;

import org.example.demo.Company;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class CompanyRepositoryInMemoryImpl implements CompanyRepository {
    private static long idCounter = 0;
    private final List<Company> companies = new ArrayList<>();

    @Override
    public void createCompany(Company company) {
        company.setId(++idCounter);
        companies.add(company);
    }

    @Override
    public Company retrieveCompanyById(long id) {
        return companies.stream().filter(company -> company.getId() == id).findFirst().orElse(null);
    }

    @Override
    public List<Company> retrieveAllCompanies() {
        return companies;
    }

    @Override
    public Company updateCompany(Company targetCompany, Company companyUpdate) {
        targetCompany.setName(companyUpdate.getName());
        return targetCompany;
    }

    @Override
    public void deleteCompanyById(long id) {
        Company targetCompany = retrieveCompanyById(id);
        companies.remove(targetCompany);
    }

    @Override
    public List<Company> findCompaniesWithPagination(int page, int size) {
        return companies.subList((page - 1) * size, page * size);
    }

    @Override
    public void cleanUp() {
        companies.clear();
        idCounter = 0;
    }
}
