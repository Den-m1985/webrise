package com.example.webrise.controller;

import com.example.webrise.dto.RegisterUserDto;
import com.example.webrise.dto.UserInfoDto;
import com.example.webrise.model.User;
import com.example.webrise.model.enums.Gender;
import com.example.webrise.model.enums.RoleEnum;
import com.example.webrise.repository.UserRepository;
import com.example.webrise.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private User user;
    private final String email = "john.doe@example.com";
    private final String password = "password";
    private final String endpointBase = "/v1/users";

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        user = new User();
        user.setFirstName("First name");
        user.setMiddleName("Middle name");
        user.setLastName("Last name");
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(RoleEnum.USER);
        user = userService.saveUser(user);
        userService.mapToDto(user);
    }

    @Test
    void createUserTest() throws Exception {
        userRepository.deleteAll();
        RegisterUserDto registerUserDto = new RegisterUserDto("Name", email, password);
        mockMvc.perform(post(endpointBase)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void getUserTest() throws Exception {
        mockMvc.perform(get(endpointBase + "/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void updateUserTest() throws Exception {
        String newEmail = "email@email.com";
        UserInfoDto userDto = new UserInfoDto("firstName",
                "middleName",
                "lastName",
                newEmail,
                Gender.FEMALE,
                RoleEnum.USER.name());
        mockMvc.perform(put(endpointBase + "/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(newEmail));
    }

    @Test
    void deleteUserTest() throws Exception {
        mockMvc.perform(delete(endpointBase + "/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
