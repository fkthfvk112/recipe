package com.recipe.myrecipe.user.integration;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.recipe.myrecipe.MyrecipeApplication;
import com.recipe.myrecipe.auth.util.JwtTokenProvider;
import com.recipe.myrecipe.user.dto.*;
import com.recipe.myrecipe.user.entity.Review;
import com.recipe.myrecipe.user.entity.User;
import com.recipe.myrecipe.user.repository.ReviewRepository;
import com.recipe.myrecipe.user.repository.UserRepository;
import com.recipe.myrecipe.user.service.UserAdditionalService;
import com.recipe.myrecipe.user.service.impl.UserAdditionalServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Autowired
    ReviewRepository reviewRepository;

    @Mock
    private Cloudinary cloudinaryMock;

    @InjectMocks
    UserAdditionalServiceImpl userAdditionalService;



    @BeforeEach
    public void setupDb() {

        String encodedPw = passwordEncoder.encode("testOne");
        User user = User.builder()
                .userId("testOne")
                .nickName("testOne")
                .password(encodedPw)
                .grantType("normal")
                .email("testOne@ggg.com")
                .build();
        userRepository.save(user);
    }

    @Test
    public void testSetup(){
        assertTrue(userRepository.getByUserId("testOne").isPresent(), "유저 데이터가 세팅되지 않았습니다.");
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
                                .userId(username).userPassword(password).grantType("normal")
                                .build())))
                .andExpect(status().isOk())
                .andReturn();

        String authrizationHeader = result.getResponse().getHeader("Authorization");
        String responseBody = result.getResponse().getContentAsString();

        assertTrue(authrizationHeader != null, "헤더가 비어있습니다.");
        assertTrue(responseBody.contains("accessToken"), "액세스 토큰이 반환되지 않았습니다.");
        assertTrue(responseBody.contains("refreshToken"), "리프래쉬 토큰이 반환되지 않았습니다.");
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

        assertTrue(jwtTokenProvider.isValidateToken(responseMap.get("newAccessToken")),
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
                .nickName("signUpTestNickName")
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

        assertTrue(signupUser.isPresent(), "등록된 사용자가 존재하지 않습니다");
        assertTrue(signupUser.get().getUserId().equals("siginUpTestOne"), "아이디가 일치하지 않습니다." );
        assertTrue(signupUser.get().getEmail().equals("siginUpTestOne@gmail.com"), "이메일이 일치하지 않습니다." );
        assertTrue(signupUser.get().getGrantType().equals("normal"), "회원가입 방식이 일치하지 않습니다." );
    }

    @Test
    @WithMockUser(username="testOne", roles={"USER_ROLE"})
    public void When_makeReview_Expect_saveIt_AND_When_recipeId_getAllReviews() throws Exception {

        //create test
        ReviewDTO reviewDTO = ReviewDTO.builder()
                .score(5)
                .recipeId(Long.valueOf(1))
                .message("이 음식 정말 맛나요!")
                .build();


        MvcResult result = mockMvc.perform(post("/review/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDTO)))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        Long reviewId = objectMapper.readValue(responseBody, Long.class);

        Review savedReview = reviewRepository.findById(reviewId).get();
        assertTrue(savedReview.getMessage().equals(reviewDTO.getMessage()), "리뷰 내용이 다릅니다.");
        assertTrue(savedReview.getScore() == reviewDTO.getScore(), "리뷰 점수가 다릅니다.");

        //get review test
        mockMvc.perform(get("/review/get?recipeId=" + 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].message", is("이 음식 정말 맛나요!")))
                .andReturn();

        reviewRepository.delete(savedReview);
    }


    @Test
    @WithMockUser(username="testOne", roles={"USER_ROLE"})
    void When_updateUserFeed_Expect_saveIt() throws Exception {
        UpdateFeedInfo updateFeedInfo = UpdateFeedInfo.builder()
                .nickName("[Test]testUserNickName")
                .userUrl("[Test]fdsd@navfds.com")
                .build();
        Map params = ObjectUtils.asMap(
                "use_filename", true,
                "unique_filename", false,
                "overwrite", true
        );


        MvcResult result = mockMvc.perform(post("/feed/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateFeedInfo)))
                .andExpect(status().isOk())
                .andReturn();

        User updatedUser =  userRepository.findByUserId("testOne").get();
        assertEquals(updatedUser.getUserUrl(), updateFeedInfo.getUserUrl(), "url이 다릅니다.");
        assertEquals(updatedUser.getNickName(), updateFeedInfo.getNickName(), "닉네임이 다릅니다.");
    }

    @Test
    void When_inputUserName_Expect_getFeedDTO() throws Exception {
        User user = userRepository.findByUserId("testOne").get();

        MvcResult result = mockMvc.perform(get("/feed/user/testOne")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("nickName", "testOne"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickName", is(user.getNickName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andReturn();

    }



    @AfterEach
    public void cleanupDb(){
        String deleteReviewQuery ="DELETE FROM review WHERE user_id = (SELECT id FROM user WHERE user_id = 'testOne')";
        ;
        jdbc.execute(deleteReviewQuery);

        String deleteUserQuery = "DELETE FROM user WHERE user_id = 'testOne'";
        jdbc.execute(deleteUserQuery);
    }
}
