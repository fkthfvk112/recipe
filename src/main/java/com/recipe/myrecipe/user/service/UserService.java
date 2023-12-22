package com.recipe.myrecipe.user.service;

import com.recipe.myrecipe.user.dto.UserLoginDTO;
import com.recipe.myrecipe.user.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserDao userDao;

    @Autowired
    UserService(UserDao userDao){
        this.userDao = userDao;
    }

    public boolean isUserExist(UserLoginDTO userLoginDTO){
        if(userDao.findById(userLoginDTO.getUserId()).isPresent()){
            return true;
        }
        return false;
    }
}
