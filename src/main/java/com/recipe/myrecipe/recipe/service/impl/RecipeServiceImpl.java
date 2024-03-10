package com.recipe.myrecipe.recipe.service.impl;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.recipe.myrecipe.auth.util.UserUtil;
import com.recipe.myrecipe.recipe.dto.*;
import com.recipe.myrecipe.recipe.dto.valueObject.RecipeOwnerInfo;
import com.recipe.myrecipe.recipe.entity.Ingredient;
import com.recipe.myrecipe.recipe.entity.Recipe;
import com.recipe.myrecipe.recipe.entity.Step;
import com.recipe.myrecipe.recipe.repository.RecipeRepository;
import com.recipe.myrecipe.recipe.service.RecipeService;
import com.recipe.myrecipe.user.dto.valueObject.UserNickName;
import com.recipe.myrecipe.user.entity.User;
import com.recipe.myrecipe.user.repository.UserRepository;
import com.recipe.myrecipe.util.DtoToEntity;
import com.recipe.myrecipe.util.ListToString;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public List<RecipeDTO> getRecentRecipes(int page, int size) {
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

    @Override
    public List<RecipeIdNamePhotoDTO> getMyRecipeInfos(String userId, int page, int size) {
        try{
            User user = userRepository.findByUserId(userId).get();
            Page<Recipe> recipePage = recipeRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId(), PageRequest.of(page, size));
            List<Recipe> recipeList = recipePage.getContent();

            List<RecipeIdNamePhotoDTO> recipeIdNamePhotoDTOList = new ArrayList<>();
            for(Recipe recipe:recipeList){
                recipeIdNamePhotoDTOList.add(
                        RecipeIdNamePhotoDTO.builder()
                                .recipeId(recipe.getId())
                                .recipeName(recipe.getRecipeName())
                                .repriPhotos(recipe.getRepriPhotos())
                                .build());
            }

            return recipeIdNamePhotoDTOList;

        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<RecipeIdNamePhotoDTO> getUserRecipeInfos(UserNickName userNickName, int page, int size) {
        try{
            User user = userRepository.findByNickName(userNickName.getNickName()).get();
            Page<Recipe> recipePage = recipeRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId(), PageRequest.of(page, size));
            List<Recipe> recipeList = recipePage.getContent();

            List<RecipeIdNamePhotoDTO> recipeIdNamePhotoDTOList = new ArrayList<>();
            for(Recipe recipe:recipeList){
                recipeIdNamePhotoDTOList.add(
                        RecipeIdNamePhotoDTO.builder()
                                .recipeId(recipe.getId())
                                .recipeName(recipe.getRecipeName())
                                .repriPhotos(recipe.getRepriPhotos())
                                .build());
            }

            return recipeIdNamePhotoDTOList;

        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<RecipeDTO> getRecipesByIngres(List<String> ingredients, int page, int size) {
        log.info("[getRecipesByIngres/service] - start");
        Page<Recipe> recipePage = recipeRepository.findAllByIngredients(ingredients, PageRequest.of(page, size));
        log.info("[selected] : " + recipePage.getContent());


        List<Recipe> recipes = recipePage.getContent();

        List<RecipeDTO> recipeDTOList = new ArrayList<>();
        for(Recipe recipe:recipes){
            recipeDTOList.add(dtoToEntity.RecipeEntityToRecipeDTO(recipe));
        }
        return recipeDTOList;
    }

    @Transactional
    @Override
    public List<RecipeDTO> getRecipesBySearchingCondition(RecipeSearchingCondition searchingCon, RecipeSortingConEnum sortingCon, int page, int size) {
        log.info("[getRecipesBySearchingCondition] - start");

        Integer searcingConMin_nullOrValue = null;
        Integer searcingConMax_nullOrValue = null;
        String searcingConName_nullOrValue =  null;
        LocalDateTime searcingConDate_nullOrValue = null;
        String searcingConMethod_nullOrValue = null;
        Boolean searcingConIngreCon_nullOrValue = null;
        String searcingConCategory_nullOrValue = null;
        String sortingConCategory_nullOrValue = null;
        String ingredintByDelim_nullOrValue = null;

        if(searchingCon != null){
            if(searchingCon.getServingsCon() != null) {
                searcingConMin_nullOrValue = searchingCon.getServingsCon().getMin();
                searcingConMax_nullOrValue = searchingCon.getServingsCon().getMax();
            }
            if(searchingCon.getIngredientNames() != null){
                ingredintByDelim_nullOrValue = ListToString.convertToDelimSeperatedString(searchingCon.getIngredientNames(), "/");
            }
            searcingConName_nullOrValue = searchingCon.getRecipeName();
            searcingConDate_nullOrValue = searchingCon.getCreatedDate();
            searcingConMethod_nullOrValue = searchingCon.getCookMethod();
            searcingConIngreCon_nullOrValue = searchingCon.isIngredientAndCon();
            searcingConCategory_nullOrValue = searchingCon.getCookCategory();
            sortingConCategory_nullOrValue = sortingCon.toString();
        }
        log.info("recipeDTOList4 - " + ingredintByDelim_nullOrValue);


        Optional<List<Recipe>> searchedRecipesOp =  recipeRepository.findRecipeByConditions(searcingConName_nullOrValue, searcingConDate_nullOrValue, searcingConMethod_nullOrValue,
                searcingConIngreCon_nullOrValue, ingredintByDelim_nullOrValue, searcingConMin_nullOrValue, searcingConMax_nullOrValue,
                searcingConCategory_nullOrValue, sortingConCategory_nullOrValue, page, size);

        if(!searchedRecipesOp.isPresent()){
            return List.of();
        }

        List<RecipeDTO> recipeDTOList = new ArrayList<>();
        for(Recipe recipe: searchedRecipesOp.get()){
            log.info("recipeDTOList3 - " + dtoToEntity.RecipeEntityToRecipeDTO(recipe));
            recipeDTOList.add(dtoToEntity.RecipeEntityToRecipeDTO(recipe));
            //recipeDTOList.add(dtoToEntity.RecipeEntityToRecipeDTO(recipe));
        }

        log.info("recipeDTOList - " + recipeDTOList);

        return recipeDTOList;
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
                    .userNickName(recipeOwner.getNickName())
                    .userPhoto(recipeOwner.getUserPhoto())
                    .userUrl(recipeOwner.getUserUrl())
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
