package com.recipe.myrecipe.user.controller;

import com.recipe.myrecipe.user.dto.ReviewDTO;
import com.recipe.myrecipe.user.service.ReviewService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/review")
public class ReviewController {

    ReviewService reviewService;

    @Autowired
    ReviewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createReview(@RequestBody @Valid ReviewDTO reviewDTO){
        Long reviewId = reviewService.saveReview(reviewDTO);

        if(reviewId == null){
            throw new IllegalArgumentException("Review ID cannot be null. Save review failed.");
        }else{
            return ResponseEntity.ok(reviewId);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<List<ReviewDTO>> getReviewsByRecipeId(@RequestParam Long recipeId){
        log.info("[getReviewsByRecipeId] - start");
        return ResponseEntity.ok(reviewService.getReviewsByRecipeId(recipeId));
    }
}
