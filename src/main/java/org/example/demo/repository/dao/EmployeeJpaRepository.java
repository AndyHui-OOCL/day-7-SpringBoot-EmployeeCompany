package org.example.demo.repository.dao;

import org.example.demo.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeJpaRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByGender(String gender);

    List<Employee> findByName(String name);
}
