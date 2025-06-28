package com.chirag.company_ms.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "REVIEW-MS")
public interface ReviewService {

    @GetMapping("/review/company")
    List<Review> getReviewList(@RequestParam Long companyId);
}
