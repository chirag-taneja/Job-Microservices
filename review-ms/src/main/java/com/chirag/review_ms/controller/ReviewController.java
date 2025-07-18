package com.chirag.review_ms.controller;


import com.chirag.review_ms.entity.Review;
import com.chirag.review_ms.messaging.ReviewMessageProducer;
import com.chirag.review_ms.repo.ReviewRepo;
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



    ReviewRepo reviewRepo;
    ReviewMessageProducer reviewMessageProducer;

    @Autowired
    public ReviewController( ReviewRepo reviewRepo,ReviewMessageProducer reviewMessageProducer) {

        this.reviewRepo = reviewRepo;
        this.reviewMessageProducer=reviewMessageProducer;
    }

    @GetMapping()
    public ResponseEntity<List<Review>> findAllJob()
    {
        return ResponseEntity.ok(reviewRepo.findAll());
    }

    @PostMapping()
    public ResponseEntity<String> saveNewJob(@RequestBody Review review, @RequestParam Long companyId){
//        Company company = comapnyRepo.findById(companyId).orElseThrow(() -> new RuntimeException("Company NOt Found"));
//        review.setCompany(company);
        Review save = reviewRepo.save(review);
        reviewMessageProducer.sendMessage(save);
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
