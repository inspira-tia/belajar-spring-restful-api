package tia_sarwoedhi.belajar_spring_restful_api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tia_sarwoedhi.belajar_spring_restful_api.entity.User;
import tia_sarwoedhi.belajar_spring_restful_api.model.UserResponse;
import tia_sarwoedhi.belajar_spring_restful_api.model.WebResponse;
import tia_sarwoedhi.belajar_spring_restful_api.model.request.RegisterUserRequest;
import tia_sarwoedhi.belajar_spring_restful_api.model.request.UpdateUserRequest;
import tia_sarwoedhi.belajar_spring_restful_api.repository.UserRepository;
import tia_sarwoedhi.belajar_spring_restful_api.security.BCrypt;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterSuccess() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();

        request.setUsername("test");
        request.setPassword("rahasia");
        request.setName("Test");

        mockMvc.perform(
                        post("/api/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                            });

                            assertEquals("OK", response.getData());
                        }
                );
    }


    @Test
    void testRegisterBadRequest() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("");
        request.setPassword("");
        request.setName("");

        mockMvc.perform(post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isBadRequest())
                .andDo(result -> {

                            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                            assertNotNull(response.getErrors());

                        }
                );
    }

    @Test
    void testRegisterDuplicate() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setName("Test");
        userRepository.save(user);

        //  RegisterUserRequest test = new RegisterUserRequest();
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("test");
        request.setPassword("rahasia");
        request.setName("Test");

        mockMvc.perform(
                        post("/api/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isBadRequest())
                .andDo(result -> {
                            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                            });

                            assertNotNull(response.getErrors());

                        }
                );
    }

    @Test
    void getUserUnAuthorized() throws Exception {
        mockMvc.perform(
                        get("/api/users/current")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("X-API-TOKEN", "notfound")
                ).andExpectAll(status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<String>>() {
                            });
                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void getUserUnAuthorizedTokenNotSend() throws Exception {
        mockMvc.perform(get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpectAll(status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<String>>() {
                            });
                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void getUserSuccess() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setName("Test");
        user.setToken("test");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setTokenExpireAt(System.currentTimeMillis() + 10000000000L);
        userRepository.save(user);
        mockMvc.perform(get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
                )
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<UserResponse> response =
                            objectMapper.readValue(result.getResponse().getContentAsString(),
                                    new TypeReference<>() {
                                    });
                    assertNotNull(response.getData());
                    assertEquals("Test", response.getData().getName());
                });
    }


    @Test
    void getUserTokenExpired() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setName("Test");
        user.setToken("test");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setTokenExpireAt(System.currentTimeMillis() - 10000000);
        userRepository.save(user);
        mockMvc.perform(get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
                )
                .andExpectAll(
                        status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<String> response =
                            objectMapper.readValue(result.getResponse().getContentAsString(),
                                    new TypeReference<>() {
                                    });
                    assertNotNull(response.getErrors());
                    //    assertEquals("Test",response.getData().getName());
                });
    }

    @Test
    void updateUserUnAuthorized() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest();

        mockMvc.perform(
                        patch("/api/users/current")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andExpectAll(status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<String>>() {
                            });
                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void updateUserSuccess() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setName("Test");
        user.setToken("test");
        user.setTokenExpireAt(System.currentTimeMillis() + 10000000000L);
        userRepository.save(user);

        UpdateUserRequest request = new UpdateUserRequest();
        request.setName("Tia");
        request.setPassword("Tia123");

        mockMvc.perform(
                        patch("/api/users/current")

                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .header("X-API-TOKEN", "test")
                ).andExpectAll(status().isOk())
                .andDo(result -> {
                    WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });
                    assertNotNull(response.getData());
                    assertEquals("Tia", response.getData().getName());
                    User userDB = userRepository.findById("test").orElse(null);
                    assertEquals("test",userDB.getUsername());
                    assertTrue(BCrypt.checkpw("Tia123",userDB.getPassword()));
                });
    }

}

