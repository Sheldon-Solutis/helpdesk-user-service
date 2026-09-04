package com.helpdesk.user_service.controller;

import com.helpdesk.user_service.dto.UserCreateDto;
import com.helpdesk.user_service.dto.UserUpdateDto;
import com.helpdesk.user_service.dto.UserResponseDto;
import com.helpdesk.user_service.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Cadastro e gestão de usuários (clientes, técnicos e administradores)")
public class UserController {

    private final UserService userService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserResponseDto>> listAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        UserResponseDto response = userService.findUserById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UserUpdateDto dto) {

        UserResponseDto response = userService.updateUser(id, dto);

        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<UserResponseDto> createUser(
            @RequestBody @Valid UserCreateDto user) {
        UserResponseDto signedUser = userService.createUser(user);

        // Padrão RESTful
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(signedUser.getId())
                .toUri();

        return ResponseEntity.created(location).body(signedUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @PostMapping("/{id}")
    public ResponseEntity<UserResponseDto> reactiveUser(@PathVariable Long id) {
        UserResponseDto response = userService.reActiveUser(id);
        return ResponseEntity.ok(response);
    }
}
