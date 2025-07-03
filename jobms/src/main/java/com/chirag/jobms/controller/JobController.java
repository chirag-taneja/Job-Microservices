package com.chirag.jobms.controller;


import com.chirag.jobms.entity.Job;
import com.chirag.jobms.external.Company;
import com.chirag.jobms.external.CompanyService;
import com.chirag.jobms.repo.JobRepo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.management.InstanceNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/job")

public class JobController {

      JobRepo jobRepo;

      RestTemplate restTemplate;
      CompanyService companyService;


    @Autowired
    public JobController(JobRepo jobRepo,RestTemplate restTemplate,CompanyService companyService) {
        this.jobRepo = jobRepo;
        this.restTemplate=restTemplate;
        this.companyService=companyService;
    }

    @GetMapping()
    public ResponseEntity<List<Job>> findAllJob()
    {
        List<Job> all = jobRepo.findAll();

        try {
            all.stream().forEach(i -> {
                i.setCompany(restTemplate.getForObject("http://COMPANY-MS/company/" + i.getCompanyId(), Company.class));
            });
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.ok(all);
    }

    @PostMapping()
    public ResponseEntity<String> saveNewJob(@RequestBody Job job,@RequestParam Long companyId){
//        Company company = comapnyRepo.findById(companyId).orElseThrow(() -> new RuntimeException("Company NOt Found"));
//        job.setCompany(company);
        jobRepo.save(job);
        return new ResponseEntity<>("job added successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @CircuitBreaker(name = "companyBreaker",fallbackMethod = "fallbackOfGetJobId")
    public  ResponseEntity<Job> getJobById(@PathVariable Long id) throws InstanceNotFoundException {
        Job job = jobRepo.findById(id).orElseThrow(() -> new RuntimeException("JOb not found"));

            job.setCompany(companyService.getCompany(job.getCompanyId()));

        return ResponseEntity.ok(job);
    }

    public  ResponseEntity<Job> fallbackOfGetJobId(Exception e) throws InstanceNotFoundException {
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJobById(@PathVariable Long id) throws InstanceNotFoundException {
        Job job = jobRepo.findById(id).orElseThrow(() -> new RuntimeException("JOb not found"));

        jobRepo.delete(job);
        return ResponseEntity.ok("deleted successfully");
    }

    @PutMapping("/{id}")
    public  ResponseEntity<Job> updateJob(@PathVariable Long id,@RequestBody Job job) throws InstanceNotFoundException {

        Job OldJOb = jobRepo.findById(id).orElseThrow(() -> new RuntimeException("JOb not found"));
                OldJOb.setTitle(job.getTitle());
                OldJOb.setDescription(job.getDescription());
                OldJOb.setLocation(job.getLocation());
                OldJOb.setMinSalary(job.getMinSalary());
                OldJOb.setMaxSalary(job.getMaxSalary());
                jobRepo.save(OldJOb);                         
                return ResponseEntity.ok(OldJOb);
    }
}
