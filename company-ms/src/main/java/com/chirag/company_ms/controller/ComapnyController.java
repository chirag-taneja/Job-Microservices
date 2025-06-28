package com.chirag.company_ms.controller;


import com.chirag.company_ms.entity.Company;
import com.chirag.company_ms.external.Review;
import com.chirag.company_ms.repo.ComapnyRepo;
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

    @Autowired
    public ComapnyController(RestTemplate restTemplate, ComapnyRepo comapnyRepo) {
        this.restTemplate = restTemplate;
        this.comapnyRepo = comapnyRepo;
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

    @GetMapping("/{id}")
    public ResponseEntity<Company> findById(@PathVariable Long id)
    {
        Company company = comapnyRepo.findById(id).orElseThrow(() -> new RuntimeException("Company NOt Found"));
        return ResponseEntity.ok(company);
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
