package com.chirag.job.controller;

import com.chirag.job.entity.Company;
import com.chirag.job.entity.Job;
import com.chirag.job.repo.ComapnyRepo;
import com.chirag.job.repo.JobRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/job")

public class JobController {

      JobRepo jobRepo;
    ComapnyRepo comapnyRepo;

    @Autowired
    public JobController(JobRepo jobRepo, ComapnyRepo comapnyRepo) {
        this.jobRepo = jobRepo;
        this.comapnyRepo = comapnyRepo;
    }


    @GetMapping()
    public ResponseEntity<List<Job>> findAllJob()
    {
        return ResponseEntity.ok(jobRepo.findAll());
    }

    @PostMapping()
    public ResponseEntity<String> saveNewJob(@RequestBody Job job,@RequestParam Long companyId){
        Company company = comapnyRepo.findById(companyId).orElseThrow(() -> new RuntimeException("Company NOt Found"));
        job.setCompany(company);
        jobRepo.save(job);
        return new ResponseEntity<>("job added successfully",HttpStatus.CREATED);
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
