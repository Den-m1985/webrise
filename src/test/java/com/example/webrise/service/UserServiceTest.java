package com.example.webrise.service;

import com.example.webrise.dto.AuthResponse;
import com.example.webrise.dto.RegisterUserDto;
import com.example.webrise.dto.UserInfoDto;
import com.example.webrise.model.User;
import com.example.webrise.model.enums.Gender;
import com.example.webrise.model.enums.RoleEnum;
import com.example.webrise.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private User user;
    private final String email = "john.doe@example.com";

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        user = new User();
        user.setFirstName("First name");
        user.setMiddleName("Middle name");
        user.setLastName("Last name");
        user.setEmail(email);
        user.setPassword("password");
        user.setRole(RoleEnum.USER);
        user = userService.saveUser(user);
    }


    @Test
    void createUserTest() {
        userRepository.deleteAll();
        RegisterUserDto registerUserDto = new RegisterUserDto("Name", email, "password");
        AuthResponse resultUser = userService.createUser(registerUserDto);
        assertEquals(email, resultUser.email());
    }

    @Test
    void saveWithoutEmail() {
        userRepository.deleteAll();
        user = new User();
        Executable action = () -> userService.saveUser(user);
        assertThrows(DataIntegrityViolationException.class, action);
    }

    @Test
    void shouldReturnUserByEmail() {
        User resultUser = userService.getUserByEmail(email);
        assertEquals(email, resultUser.getEmail());
    }

    @Test
    void shouldReturnUserById() {
        User resultUser = userService.findUserById(user.getId());
        assertEquals(user.getId(), resultUser.getId());
    }

    @Test
    void testGetAllUsers() {
        List<User> result = userService.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void shouldThrowUserNotFound() {
        assertThatThrownBy(() -> userService.findUserById(user.getId() + 1))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void updateUserTest() {
        String newEmail = "email@email.com";
        UserInfoDto userDto = new UserInfoDto("firstName",
                "middleName",
                "lastName",
                newEmail,
                Gender.FEMALE,
                RoleEnum.USER.name());
        UserInfoDto resultUser = userService.updateUser(user.getId(), userDto);
        assertEquals(newEmail, resultUser.email());
    }

    @Test
    void deleteUserTest() {
        User userFromBD = userService.findUserById(user.getId());
        userService.deleteUser(userFromBD.getId());
        assertThatThrownBy(() -> userService.findUserById(user.getId()))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
