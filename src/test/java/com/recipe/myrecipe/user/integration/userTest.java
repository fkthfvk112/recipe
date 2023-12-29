package com.recipe.myrecipe.user.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.recipe.myrecipe.MyrecipeApplication;
import com.recipe.myrecipe.auth.util.JwtTokenProvider;
import com.recipe.myrecipe.user.dto.UserAndRefreshDTO;
import com.recipe.myrecipe.user.dto.UserLoginDTO;
import com.recipe.myrecipe.user.dto.UserSiginUpDTO;
import com.recipe.myrecipe.user.entity.User;
import com.recipe.myrecipe.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = MyrecipeApplication.class)
@Transactional
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

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setupDb() {
        String encodedPw = passwordEncoder.encode("testOne");
        String insertQuery = "INSERT INTO user(user_id, password, grant_type, email) " +
                "VALUES('testOne', '" + encodedPw + "', 'normal', 'testOne@ggg.com')";
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

        MvcResult result = mockMvc.perform(post("/sign-api/sign-in").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserLoginDTO.builder()
                                .userId(username).userPassword(password).grantType("normal").email("testOne@ggg.com")
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
    void When_requestRefreshToken_Expect_tokenOrError() throws Exception{
        String validRefreshToken = jwtTokenProvider.generateAccessToken("testUser",List.of("USER"));
        String invalidRefreshToken = "invalidTestToken";

        System.out.println("토큰" + validRefreshToken);

        UserAndRefreshDTO dto = UserAndRefreshDTO.builder()
                .refreshToken(validRefreshToken)
                .roles(List.of("USER"))
                .userId("TestUserId").build();


        MvcResult result = mockMvc.perform(post("/sign-api/get-accesstoken")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("반환값 : " + responseBody);
        Map<String, String> responseMap = objectMapper.readValue(responseBody, Map.class);

        Assertions.assertTrue(jwtTokenProvider.isValidateToken(responseMap.get("newAccessToken")),
                "발급 받은 토큰이 유효하지 않습니다");
    }

    @Test
    public void when_loginInfoIsNotCollect_Expect_exception() throws Exception {
        String username = "testZero";//not exist
        String password = "testZero";//not exist
        mockMvc.perform(post("/sign-api/sign-in")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", is(403)))
                .andExpect(jsonPath("$.message", is("User info does not correct")));
    }

    @Test
    public void when_signupInfoIsCorrect_Expect_success() throws Exception{
        UserSiginUpDTO userSiginUpDTO = UserSiginUpDTO.builder()
                .userId("siginUpTestOne")
                .userPassword("siginUpTestOne")
                .email("siginUpTestOne@gmail.com")
                .grantType("normal")
                .build();


        mockMvc.perform(post("/sign-api/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSiginUpDTO)))
                .andExpect(status().isOk())
                .andReturn();

        Optional<User> signupUser = userRepository.getByUserId("siginUpTestOne");

        Assertions.assertTrue(signupUser.isPresent(), "등록된 사용자가 존재하지 않습니다");
        Assertions.assertTrue(signupUser.get().getUserId().equals("siginUpTestOne"), "아이디가 일치하지 않습니다." );
        Assertions.assertTrue(signupUser.get().getEmail().equals("siginUpTestOne@gmail.com"), "이메일이 일치하지 않습니다." );
        Assertions.assertTrue(signupUser.get().getGrantType().equals("normal"), "회원가입 방식이 일치하지 않습니다." );
    }

    @AfterEach
    public void cleanupDb(){
        String deleteQuery = "DELETE FROM user WHERE user_id = 'testOne'";
        jdbc.execute(deleteQuery);
    }
}
