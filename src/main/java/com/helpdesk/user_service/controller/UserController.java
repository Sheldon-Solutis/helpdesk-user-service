package com.helpdesk.user_service.controller;

import com.helpdesk.user_service.dto.UserCreateDto;
import com.helpdesk.user_service.dto.UserPatchDto;
import com.helpdesk.user_service.dto.UserResponseDto;
import com.helpdesk.user_service.model.User;
import com.helpdesk.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserResponseDto>> listAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/^{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        UserResponseDto response = userService.findUserById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @RequestBody UserPatchDto dto) {
        UserResponseDto response = userService.updateUser(id, dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserCreateDto user) {
        UserResponseDto signedUser = userService.createUser(user);

        // Padrão RESTful
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(signedUser.getId())
                .toUri();

        return ResponseEntity.created(location).body(signedUser);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String email) {
        userService.deleteUserByEmail(email);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @PostMapping("/{email}")
    public ResponseEntity<UserResponseDto> reactiveUser(@PathVariable String email) {
        UserResponseDto response = userService.reActiveUser(email);
        return ResponseEntity.ok(response);
    }
}
