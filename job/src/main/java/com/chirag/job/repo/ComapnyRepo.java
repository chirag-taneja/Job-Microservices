package com.chirag.job.repo;

import com.chirag.job.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComapnyRepo  extends JpaRepository<Company,Long> {
}
