package com.recipe.myrecipe.user.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.recipe.myrecipe.MyrecipeApplication;
import com.recipe.myrecipe.auth.util.JwtTokenProvider;
import com.recipe.myrecipe.user.dto.UserLoginDTO;
import com.recipe.myrecipe.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = MyrecipeApplication.class)
@AutoConfigureMockMvc
public class userTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void setupDb() {
        String insertQuery = "INSERT INTO user(user_id, password, grant_type, email, role) " +
                "VALUES('testOne', 'testOne', 'normal', 'testOne@ggg.com', 'USER')";
        jdbc.execute(insertQuery);
    }

    @Test
    public void testSetup(){
        Assertions.assertTrue(userRepository.getByUserId("testOne").isPresent(), "유저 데이터가 세팅되지 않았습니다.");
    }

    @Test
    public void when_loginInfoIsCollect_Expect_token() throws Exception {
        String username = "testOne";
        String password = "testOne";
        String accessToken = "testAccessToken";
        String refreshToekn = "testRefreshToken";
        Authentication authentication = mock(Authentication.class);
        given(authentication.getName()).willReturn(username);

//        given(jwtUtil.generateAccessToken(authentication)).willReturn(accessToken);
//        given(jwtUtil.generateRefreshToken(authentication)).willReturn(refreshToekn);

        MvcResult result = mockMvc.perform(post("/user/login").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UserLoginDTO.builder()
                        .userId(username).userPassword(password).grantType("normal")
                        .build())))
                .andExpect(status().isOk())
                .andReturn();

        String authrizationHeader = result.getResponse().getHeader("Authorization");
        String responseBody = result.getResponse().getContentAsString();

        Assertions.assertTrue(authrizationHeader != null, "헤더가 비어있습니다.");
        Assertions.assertTrue(responseBody.contains("accessToken"), "액세스 토큰이 반환되지 않았습니다.");
        Assertions.assertTrue(responseBody.contains("refreshToken"), "리프래쉬 토큰이 반환되지 않았습니다.");
    }

    @Test
    public void when_loginInfoIsNotCollect_Expect_exception() throws Exception {
        String username = "testZero";//not exist
        String password = "testZero";//not exist
        mockMvc.perform(post("/user/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", is(403)))
                .andExpect(jsonPath("$.message", is("User info does not correct")));
    }

    @BeforeEach
    public void cleanupDb(){
        String deleteQuery = "DELETE FROM user WHERE user_id = 'testOne'";
        jdbc.execute(deleteQuery);
    }
}
