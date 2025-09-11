package org.example.demo.repository.company;

import org.example.demo.Company;
import org.example.demo.repository.dao.CompanyJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CompanyRepositoryDbImpl implements CompanyRepository {
    @Autowired
    CompanyJpaRepository repository;

    @Override
    public void createCompany(Company company) {
        repository.save(company);
    }

    @Override
    public Company retrieveCompanyById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Company> retrieveAllCompanies() {
        return repository.findAll();
    }

    @Override
    public Company updateCompany(Company targetCompany, Company companyUpdate) {
        return repository.save(companyUpdate);
    }

    @Override
    public void deleteCompanyById(long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Company> findCompaniesWithPagination(int page, int size) {
        return repository.findAll(PageRequest.of(page - 1, size)).stream().toList();
    }

    @Override
    public void cleanUp() {
        repository.deleteAll();
    }
}
