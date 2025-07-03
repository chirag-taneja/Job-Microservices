package com.chirag.company_ms.controller;


import com.chirag.company_ms.entity.Company;
import com.chirag.company_ms.external.Review;
import com.chirag.company_ms.external.ReviewService;
import com.chirag.company_ms.repo.ComapnyRepo;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/company")
public class ComapnyController {

    RestTemplate restTemplate;

    ComapnyRepo comapnyRepo;
    ReviewService reviewService;

    int attempt=0;
    @Autowired
    public ComapnyController(RestTemplate restTemplate, ComapnyRepo comapnyRepo,ReviewService reviewService) {
        this.restTemplate = restTemplate;
        this.comapnyRepo = comapnyRepo;
        this.reviewService=reviewService;
    }

    @GetMapping()
    public ResponseEntity<List<Company>> findAll()
    {
        List<Company> all = comapnyRepo.findAll();
        all.forEach(i->{
            ResponseEntity<List<Review>> listResponseEntity = restTemplate.exchange("http://REVIEW-MS/review/company?companyId=" + i.getId(), HttpMethod.GET, null, new ParameterizedTypeReference<List<Review>>() {
            });
            i.setReviews(listResponseEntity.getBody());
        });
        return  ResponseEntity.ok(all);
    }

    @Retry(name = "reviewBreaker",fallbackMethod = "fallbackOfGetCompanyById")
    @GetMapping("/{id}")
    public ResponseEntity<Company> findById(@PathVariable Long id)
    {

        System.out.println("attempt"+ ++attempt);
        Company company = comapnyRepo.findById(id).orElseThrow(() -> new RuntimeException("Company NOt Found"));
        company.setReviews(reviewService.getReviewList(company.getId()));
        return ResponseEntity.ok(company);
    }

    public ResponseEntity<Company> fallbackOfGetCompanyById(Exception e){
        return ResponseEntity.notFound().build();
    }

    @PostMapping()
    public ResponseEntity<Company> saveCompany(@RequestBody Company company)
    {
        return ResponseEntity.ok(comapnyRepo.save(company));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@RequestBody Company company,@PathVariable Long id)
    {
        Company companyToUpdate = comapnyRepo.findById(id).orElseThrow(() -> new RuntimeException("Company NOt Found"));
        companyToUpdate.setName(company.getName());
        companyToUpdate.setDescription(company.getDescription());
        return ResponseEntity.ok(comapnyRepo.save(companyToUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable Long id)
    {
        Company company = comapnyRepo.findById(id).orElseThrow(() -> new RuntimeException("Company NOt Found"));
        comapnyRepo.delete(company);
        return ResponseEntity.ok("deleted successfully");
    }


}
