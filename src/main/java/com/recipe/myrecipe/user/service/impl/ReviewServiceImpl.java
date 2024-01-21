package com.recipe.myrecipe.user.service.impl;

import com.recipe.myrecipe.error.BusinessException;
import com.recipe.myrecipe.error.ErrorCode;
import com.recipe.myrecipe.recipe.entity.Recipe;
import com.recipe.myrecipe.recipe.repository.RecipeRepository;
import com.recipe.myrecipe.user.dto.ReviewDTO;
import com.recipe.myrecipe.user.dto.ReviewResultDTO;
import com.recipe.myrecipe.user.dto.valueObject.ReviewUserDTO;
import com.recipe.myrecipe.user.entity.Review;
import com.recipe.myrecipe.user.entity.User;
import com.recipe.myrecipe.user.repository.ReviewRepository;
import com.recipe.myrecipe.user.repository.UserRepository;
import com.recipe.myrecipe.user.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private ReviewRepository reviewRepository;
    private UserRepository userRepository;
    private RecipeRepository recipeRepository;

    @Autowired
    ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository,
                      RecipeRepository recipeRepository){
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.recipeRepository =recipeRepository;
    }
    @Override
    public Long saveReview(ReviewDTO reviewDTO) {
        try {
            log.info("[saveReview] - start");

            //get user entity
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication == null){
                log.info("[saveReview] - Error : auth is null");
                throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
            }

            String userName = authentication.getName();
            User accessUser =userRepository.findByUserId(userName).get();
            log.info("[saveReview] - get user info");

            //get recipe entity
            Long recipeId = reviewDTO.getRecipeId();
            Recipe recipe = recipeRepository.findById(recipeId).get();
            log.info("[saveReview] - get recie info");


            //save review
            Review review = Review.builder()
                    .user(accessUser)
                    .score(reviewDTO.getScore())
                    .message(reviewDTO.getMessage())
                    .recipe(recipe)
                    .build();

            Review savedReview = reviewRepository.save(review);
            log.info("[saveReview] - success save review");

            return savedReview.getId();

        } catch (Exception e){
            log.error("[saveReview] - Catch Error", e);
            return null;
        }
    }

    @Override
    public List<ReviewResultDTO> getReviewsByRecipeId(Long recipeId) {
        Optional<List<Review>> optionalReviewListews = reviewRepository.findByRecipeId(recipeId);
        List<ReviewResultDTO> reviewResultDTOList = new ArrayList<>();

        for(Review review:optionalReviewListews.get()){
            ReviewResultDTO dto = ReviewResultDTO.builder()
                    .userInfo(ReviewUserDTO.builder()
                            .userNickName(review.getUser().getNickName())
                            .userID(review.getUser().getId())
                            .build())
                    .message(review.getMessage())
                    .createdAt(review.getCreatedAt())
                    .recipeId(recipeId)
                    .score(review.getScore()).build();

            reviewResultDTOList.add(dto);
        }

        return reviewResultDTOList;
    }
}
