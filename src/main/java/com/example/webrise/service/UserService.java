package com.example.webrise.service;

import com.example.webrise.dto.AuthResponse;
import com.example.webrise.dto.RegisterUserDto;
import com.example.webrise.dto.UserInfoDto;
import com.example.webrise.model.User;
import com.example.webrise.model.enums.RoleEnum;
import com.example.webrise.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));
    }

    public User getUserByEmail(String userEmail) {
        return findUserByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User with mail: " + userEmail + " not found"));
    }

    public Optional<User> findUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public UserInfoDto getUserInfo(Integer userId) {
        User user = findUserById(userId);
        return new UserInfoDto(
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName(),
                user.getEmail(),
                user.getGender(),
                user.getRole().name()
        );
    }

    public AuthResponse createUser(RegisterUserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.name());
        user.setEmail(userDto.email());
        user.setPassword(userDto.password());
        user.setRole(RoleEnum.USER);
        user = saveUser(user);
        log.info("Created user with id {}", user.getId());
        return new AuthResponse(user.getId(), user.getFirstName(), user.getEmail());
    }


    public UserInfoDto updateUser(Integer id, UserInfoDto userDto) {
        User user = findUserById(id);
        user.setEmail(userDto.email());
        user = saveUser(user);
        log.info("Updated user with id {}", user.getId());
        return mapToDto(user);
    }


    public void deleteUser(Integer id) {
        findUserById(id);
        userRepository.deleteById(id);
        log.info("Deleted user with id {}", id);
    }

    public UserInfoDto mapToDto(User user) {
        return new UserInfoDto(user.getFirstName(),
                user.getMiddleName(),
                user.getLastName(),
                user.getEmail(),
                user.getGender(),
                user.getRole().name());
    }
}