package com.chirag.company_ms.repo;


import com.chirag.company_ms.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComapnyRepo  extends JpaRepository<Company,Long> {
}
