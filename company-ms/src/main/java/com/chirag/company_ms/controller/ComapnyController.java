package com.chirag.company_ms.controller;


import com.chirag.company_ms.entity.Company;
import com.chirag.company_ms.repo.ComapnyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
public class ComapnyController {

    ComapnyRepo comapnyRepo;

    @Autowired
    public ComapnyController(ComapnyRepo comapnyRepo) {
        this.comapnyRepo = comapnyRepo;
    }

    @GetMapping()
    public ResponseEntity<List<Company>> findAll()
    {
        return  ResponseEntity.ok(comapnyRepo.findAll());
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
