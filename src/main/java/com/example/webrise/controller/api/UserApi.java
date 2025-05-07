package com.example.webrise.controller.api;

import com.example.webrise.dto.AuthResponse;
import com.example.webrise.dto.RegisterUserDto;
import com.example.webrise.dto.UserInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Authentication Controller")
public interface UserApi {

    @Operation(summary = "User Registration")
    ResponseEntity<AuthResponse> registerUser(@RequestBody @Valid RegisterUserDto request);

    @Operation(summary = "User get")
    ResponseEntity<UserInfoDto> getUser(@PathVariable Integer userId);

    @Operation(summary = "User update")
    ResponseEntity<UserInfoDto> updateUser(@PathVariable Integer userId, @RequestBody UserInfoDto userInfoDto);

    @Operation(summary = "User delete")
    ResponseEntity<Void> deleteUser(@PathVariable Integer userId);
}
