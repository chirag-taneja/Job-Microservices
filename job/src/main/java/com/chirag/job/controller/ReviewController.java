package com.chirag.job.controller;

import com.chirag.job.entity.Company;
import com.chirag.job.entity.Review;
import com.chirag.job.repo.ComapnyRepo;
import com.chirag.job.repo.JobRepo;
import com.chirag.job.repo.ReviewRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;
import java.security.PublicKey;
import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {


    ComapnyRepo comapnyRepo;
    ReviewRepo reviewRepo;

    @Autowired
    public ReviewController(ComapnyRepo comapnyRepo, ReviewRepo reviewRepo) {
        this.comapnyRepo = comapnyRepo;
        this.reviewRepo = reviewRepo;
    }

    @GetMapping()
    public ResponseEntity<List<Review>> findAllJob()
    {
        return ResponseEntity.ok(reviewRepo.findAll());
    }

    @PostMapping()
    public ResponseEntity<String> saveNewJob(@RequestBody Review review, @RequestParam Long companyId){
        Company company = comapnyRepo.findById(companyId).orElseThrow(() -> new RuntimeException("Company NOt Found"));
        review.setCompany(company);
        reviewRepo.save(review);
        return new ResponseEntity<>("Review added successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<Review> getJobById(@PathVariable Long id) throws InstanceNotFoundException {
        Review review = reviewRepo.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJobById(@PathVariable Long id) throws InstanceNotFoundException {
        Review review = reviewRepo.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
        reviewRepo.delete(review);
        return ResponseEntity.ok("deleted successfully");
    }

    @PutMapping("/{id}")
    public  ResponseEntity<Review> updateJob(@PathVariable Long id,@RequestBody Review review) throws InstanceNotFoundException {
        Review OldReview = reviewRepo.findById(id).orElseThrow(() -> new RuntimeException("JOb not found"));
        OldReview.setTitle(review.getTitle());
        OldReview.setDescription(review.getDescription());
        OldReview.setRating(review.getRating());
        reviewRepo.save(OldReview);
        return ResponseEntity.ok(OldReview);
    }

    @GetMapping("/company")
    public ResponseEntity<List<Review>> findReviewByCompanyId(@RequestParam Long companyId){
        return ResponseEntity.ok(reviewRepo.findByCompanyId(companyId));
    }
}
