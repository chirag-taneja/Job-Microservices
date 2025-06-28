package com.chirag.jobms.controller;


import com.chirag.jobms.entity.Job;
import com.chirag.jobms.external.Company;
import com.chirag.jobms.repo.JobRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.management.InstanceNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/job")

public class JobController {

      JobRepo jobRepo;

      RestTemplate restTemplate;


    @Autowired
    public JobController(JobRepo jobRepo,RestTemplate restTemplate) {
        this.jobRepo = jobRepo;
        this.restTemplate=restTemplate;
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
    public  ResponseEntity<Job> getJobById(@PathVariable Long id) throws InstanceNotFoundException {
        Job job = jobRepo.findById(id).orElseThrow(() -> new RuntimeException("JOb not found"));
        return ResponseEntity.ok(job);
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
