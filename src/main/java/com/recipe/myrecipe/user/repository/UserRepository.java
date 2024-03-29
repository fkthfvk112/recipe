package com.recipe.myrecipe.user.repository;

import com.recipe.myrecipe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getByUserId(String userId);
    Optional<User> findByUserId(String username);

    Optional<User> findByNickName(String nickName);


}
