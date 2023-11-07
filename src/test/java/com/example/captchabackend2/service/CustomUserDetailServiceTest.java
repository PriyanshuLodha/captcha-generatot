package com.example.captchabackend2.service;

import com.example.captchabackend2.entity.UserInfo;
import com.example.captchabackend2.repo.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailServiceTest {

    @Mock
    private UserRepo userRepo;

    private CustomUserDetailService customUserDetailService;

    @BeforeEach
    void setUp(){
        customUserDetailService=new CustomUserDetailService(userRepo);

    }
    ObjectMapper objectMapper=new ObjectMapper();
    JsonNode userJson=null;

    File file=new File("src/main/java/com/example/captchabackend2/testfolder/test-data.json");
    File file2=new File("src/main/java/com/example/captchabackend2/testfolder/test-data-result.json");

    @Test
    void saveUserDetails() throws JsonProcessingException {
        try {
            userJson=objectMapper.readTree(file);
        }catch (IOException e){
            e.printStackTrace();
        }
        if(userJson.isArray()){
            ArgumentCaptor<UserInfo> userInfoArgumentCaptor=ArgumentCaptor.forClass(UserInfo.class);
            for(JsonNode item:userJson){
                UserInfo user=UserInfo.builder().username(item.get("username").asText()).password(item.get("password").asText()).email(item.get("email").asText()).build();
                customUserDetailService.saveUserDetails(user);

            }

            verify(userRepo, times(userJson.size())).save(userInfoArgumentCaptor.capture());
            List<UserInfo> capturedUsers = userInfoArgumentCaptor.getAllValues();
            ObjectMapper objectMapper2 = new ObjectMapper();
            List<ObjectNode> objectNodes=new ArrayList<>();
            for(UserInfo item:capturedUsers){
                ObjectNode jsonNode2 = objectMapper.createObjectNode();
                jsonNode2.putNull("id");
                jsonNode2.put("username", item.getUsername());
                jsonNode2.put("password", item.getPassword());  // Corrected from item.getUsername()
                jsonNode2.put("email", item.getEmail());
                objectNodes.add(jsonNode2);
            }

            // Now, you can assert each captured user in the loop if needed
            for (int i = 0; i < userJson.size(); i++) {


                String actualJson=objectNodes.get(i).toString();
                String expectedJson = userJson.get(i).toString();
                assertEquals(expectedJson, actualJson);
                if (userJson.get(i) instanceof ObjectNode) {
                    // Update the JSON field "test-case" to "true" for each object
                    ((ObjectNode) userJson.get(i)).put("save-user-detail","true");
                }
            }
            try {
                objectMapper.writeValue(file2, userJson);
            }catch (IOException e){
                e.printStackTrace();
            }

        }

    }


    @Test
    void loadUserByUsername() {
    }

    @Test
    void loadIdbyUsername() {

        UserInfo user = UserInfo.builder().username("abc").password("1234").email("abc@gmail.com").build();

        when(userRepo.findByUsername("abc")).thenReturn(user);

        UserInfo testUser = customUserDetailService.loadIdbyUsername("abc");


        verify(userRepo).findByUsername("abc");


        assertThat(testUser).isEqualTo(user);

    }

    @Test
    void loginUser() {
        UserInfo user = UserInfo.builder().username("abc").password("1234").email("abc@gmail.com").build();

        // Mock the behavior of userRepo.findByUsername to return the 'user' object
        when(userRepo.findByUsername("abc")).thenReturn(user);

        UserInfo testUser = customUserDetailService.loadIdbyUsername("abc");

        // Verify that userRepo.findByUsername was called with "abc"
        verify(userRepo).findByUsername("abc");


        // Assert that testUser is equal to the 'user' object
        assertThat(testUser).isEqualTo(user);
    }
    @Test
    void loginUser_WithValidCredentials_ShouldReturnUserId() {
        //given
        String username="abc";
        String password="1234";
        UserInfo user=UserInfo.builder().username(username).id(1).email("dump@gmail.com").password(password).build();
        when(userRepo.findByUsername(username)).thenReturn(user);
        String result=customUserDetailService.loginUser(username,password);
        verify(userRepo).findByUsername(username);
        assertEquals("1",result);
        //when
        //then
    }
    @Test
    void loginUser_WithIncorrectPassword_ShouldReturnErrorMessage() {
        String username = "abc";
        String password = "1234";

        UserInfo user = UserInfo.builder().username(username).password("incorrect_password").email("abc@gmail.com").id(1).build();

        // Mock the behavior of userRepo.findByUsername to return the 'user' object
        when(userRepo.findByUsername(username)).thenReturn(user);

        String result = customUserDetailService.loginUser(username, password);

        // Verify that userRepo.findByUsername was called with the correct username
        verify(userRepo).findByUsername(username);

        // Assert that the result is the error message
        assertEquals("Username or password incorrect", result);
    }
    @Test
    void loadUserbyName() {
    }
}

