package com.recipe.myrecipe.recipe.service.impl;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.recipe.myrecipe.auth.util.UserUtil;
import com.recipe.myrecipe.recipe.dto.GetDetailRecipeDTO;
import com.recipe.myrecipe.recipe.dto.IngredientDTO;
import com.recipe.myrecipe.recipe.dto.RecipeDTO;
import com.recipe.myrecipe.recipe.dto.StepDTO;
import com.recipe.myrecipe.recipe.dto.valueObject.RecipeOwnerInfo;
import com.recipe.myrecipe.recipe.entity.Ingredient;
import com.recipe.myrecipe.recipe.entity.Recipe;
import com.recipe.myrecipe.recipe.entity.Step;
import com.recipe.myrecipe.recipe.repository.RecipeRepository;
import com.recipe.myrecipe.recipe.service.RecipeService;
import com.recipe.myrecipe.user.entity.User;
import com.recipe.myrecipe.user.repository.UserRepository;
import com.recipe.myrecipe.util.DtoToEntity;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {

    private ModelMapper modelMapper;
    private UserRepository userRepository;
    private RecipeRepository recipeRepository;
    private DtoToEntity dtoToEntity;
    private UserUtil userUtil;

    private Cloudinary cloudinary;
    @Autowired
    RecipeServiceImpl(ModelMapper modelMapper, RecipeRepository recipeRepository,
                      DtoToEntity dtoToEntity, UserUtil userUtil,
                      UserRepository userRepository, Cloudinary cloudinary){
        this.modelMapper = modelMapper;
        this.recipeRepository = recipeRepository;
        this.dtoToEntity = dtoToEntity;
        this.userUtil = userUtil;
        this.userRepository = userRepository;
        this.cloudinary = cloudinary;
    }

    public boolean saveRecipe(RecipeDTO recipeDTO){
        try{
            log.info("[saveRecipe] - start");

            String userId = userUtil.getUserId();
            Recipe recipe = dtoToEntity.RecipeDtoToRecipeEntity(recipeDTO);

            log.info("[saveRecipe] - get user start");
            User uploadUser = userRepository.getByUserId(userId).get();
            log.info("[saveRecipe] - get user done");

            //set username
            recipe.setUser(uploadUser);

            //set saved repri urls
            List<String> savedRepriUrls = saveImageListToAPIserver(recipeDTO.getRepriPhotos());
            recipe.setRepriPhotos(savedRepriUrls);

            //set saved step img urls
            List<StepDTO> steps = recipeDTO.getSteps();
            for(int i = 0; i < steps.size(); i++){
                if(steps.get(i).getPhoto().length() >= 10){//has img
                    String stepImgUrl = saveImageToAPIserver(steps.get(i).getPhoto());
                    recipe.getSteps().get(i).setPhoto(stepImgUrl);
                }
            }

            log.info("[saveRecipe] - preJob done");
            recipeRepository.save(recipe);
            log.info("[saveRecipe] - data saved at db");

            return true;
        } catch (Exception e){
            log.info("[RecipeServiceImpl/saveRecipe] - save fail {}", e);
            return false;
        }
    }

    public List<String> saveImageListToAPIserver(List<String> imgs) throws IOException {
        List<String> updatedImgs = new ArrayList<>(imgs);

        //save repri img
        for(int i = 0; i < updatedImgs.size(); i++){
            Map params = ObjectUtils.asMap(
                    "use_filename", true,
                    "unique_filename", false,
                    "overwrite", true
            );
            String img = updatedImgs.get(i);
            Object imgObj = (Object)img;
            try{
                Map<?, ?> uploadResult = cloudinary.uploader().upload(imgObj, ObjectUtils.emptyMap());
                log.info("[saveImageToAPIserver] - saved repri Img success");
                updatedImgs.set(i, uploadResult.get("secure_url").toString());
            } catch (IOException e){
                log.error("[saveImageToAPIserver] - IOException", e);
                throw new IOException("Error during image upload", e);
            }
        }

        //save
        return updatedImgs;
    }

    public String saveImageToAPIserver(String img) throws IOException {

        String savedImg = "";

        //save repri img
        Map params = ObjectUtils.asMap(
                "use_filename", true,
                "unique_filename", false,
                "overwrite", true
        );

        Object imgObj = (Object)img;
        try{
            Map<?, ?> uploadResult = cloudinary.uploader().upload(imgObj, ObjectUtils.emptyMap());
            log.info("[saveImageToAPIserver] - saved repri Img success");
            savedImg = uploadResult.get("secure_url").toString();
        } catch (IOException e){
            log.error("[saveImageToAPIserver] - IOException", e);
            throw new IOException("Error during image upload", e);
        }


        //save
        return savedImg;
    }

    @Override
    public List<RecipeDTO> getRecentUsers(int page, int size) {
        Page<Recipe> recipePage = recipeRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size));
        List<Recipe> recipes = recipePage.getContent();

        List<RecipeDTO> recipeDTOList = new ArrayList<>();
        for(Recipe recipe:recipes){
            recipeDTOList.add(dtoToEntity.RecipeEntityToRecipeDTO(recipe));
        }
        return recipeDTOList;
    }

    @Override
    public boolean updateRecipeViews(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post id: " + recipeId));

        recipe.setView(recipe.getView() + 1);

        recipeRepository.save(recipe);

        return true;
    }

    public RecipeDTO getRecipeById(Long recipeId){
        try{
            log.info("[getRecipeById] - start");
            return dtoToEntity.RecipeEntityToRecipeDTO(recipeRepository.findById(recipeId).get());
        }
        catch (Exception e){
            log.info("[getRecipeById] - error : {}", e);
            return null;
        }
    }

    public GetDetailRecipeDTO getDetailRecipeById(Long recipeId){
        try{
            log.info("[getDetailRecipeById] - start with Id : {}", recipeId);

            Recipe recipe = recipeRepository.findById(recipeId).get();
            User recipeOwner = recipe.getUser();
            //수정 - 추가 : 유저 관련 작업
            log.info("[getDetailRecipeById] - recipe : {}", recipe);

            RecipeDTO recipeDTO = dtoToEntity.RecipeEntityToRecipeDTO(recipe);
            RecipeOwnerInfo recipeOwnerInfo = RecipeOwnerInfo.builder()
                    .userId(recipeOwner.getUserId())
                    .build();

            log.info("[getDetailRecipeById] - dto : {}", recipeDTO);

            return GetDetailRecipeDTO.builder()
                    .recipeDTO(recipeDTO)
                    .recipeOwnerInfo(recipeOwnerInfo)
                    .build();
        }catch (Exception e){
            log.info("[getDetailRecipeById] - error : {}", e);
            return null;
        }
    }
}
