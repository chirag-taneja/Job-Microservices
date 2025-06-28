package com.chirag.review_ms.repo;


import com.chirag.review_ms.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepo extends JpaRepository<Review,Long> {

    //get all review by companyId
    public List<Review> findByCompanyId(Long companyId);
}
