package com.helpdesk.user_service.controller;

import com.helpdesk.user_service.dto.UserCreateDto;
import com.helpdesk.user_service.dto.UserResponseDto;
import com.helpdesk.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> listAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping()
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserCreateDto user) {
        UserResponseDto signedUser = userService.createUser(user);
        return ResponseEntity.ok(signedUser);
    }
}
