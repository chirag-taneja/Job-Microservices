package com.chirag.company_ms.messaging;

import com.chirag.company_ms.entity.Company;
import com.chirag.company_ms.external.Review;
import com.chirag.company_ms.external.ReviewService;
import com.chirag.company_ms.repo.ComapnyRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewMessageConsumer {


    private ComapnyRepo comapnyRepo;
    ReviewService reviewService;

    @Autowired
    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Autowired
    public ReviewMessageConsumer(ComapnyRepo comapnyRepo) {
        this.comapnyRepo = comapnyRepo;
    }

    @RabbitListener(queues = "companyRatingQueue")
    public  void consumeMessage(Review review)
    {

        Optional<Company> company = comapnyRepo.findById(review.getCompanyId());
        if (company.isPresent())
        {
            List<Review> reviewList = reviewService.getReviewList(review.getCompanyId());
            double avg = reviewList.stream().mapToDouble(Review::getRating).average().orElse(0.0);
            company.get().setRating(avg);
            Company save = comapnyRepo.save(company.get());

        }

    }
}
