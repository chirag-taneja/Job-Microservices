package com.chirag.job.repo;

import com.chirag.job.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepo extends JpaRepository<Review,Long> {

    //get all review by companyId
    public List<Review> findByCompanyId(Long companyId);
}
