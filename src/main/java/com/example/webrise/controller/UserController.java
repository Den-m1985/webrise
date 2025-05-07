package com.example.webrise.controller;

import com.example.webrise.controller.api.UserApi;
import com.example.webrise.dto.AuthResponse;
import com.example.webrise.dto.RegisterUserDto;
import com.example.webrise.dto.UserInfoDto;
import com.example.webrise.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<AuthResponse> registerUser(@RequestBody @Valid RegisterUserDto request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserInfoDto> getUser(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUserInfo(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserInfoDto> updateUser(@PathVariable Integer id, @RequestBody UserInfoDto userInfoDto) {
        return ResponseEntity.ok(userService.updateUser(id, userInfoDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
