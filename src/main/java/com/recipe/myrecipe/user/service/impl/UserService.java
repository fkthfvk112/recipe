package com.recipe.myrecipe.user.service.impl;

import com.recipe.myrecipe.user.dto.UserLoginDTO;
import com.recipe.myrecipe.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    UserRepository userRepository;

    @Autowired
    UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public boolean isUserExist(UserLoginDTO userLoginDTO){
        if(userRepository.getByUserId(userLoginDTO.getUserId()).isPresent()){
            return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getByUserId(username).get();
    }
}
