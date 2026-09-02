package com.helpdesk.user_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helpdesk.user_service.dto.UserCreateDto;
import com.helpdesk.user_service.dto.UserResponseDto;
import com.helpdesk.user_service.enums.UserRole;
import com.helpdesk.user_service.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void createUser_deveRetornar201ComCorpoValido() throws Exception {
        UserCreateDto request = new UserCreateDto();
        request.setName("Maria Lima");
        request.setEmail("maria@helpdesk.com");
        request.setRole(UserRole.CLIENT);

        UserResponseDto response = new UserResponseDto();
        response.setId(1L);
        response.setName("Maria Lima");
        response.setRole(UserRole.CLIENT);
        response.setActive(true);

        when(userService.createUser(any(UserCreateDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Maria Lima"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void createUser_deveRetornar400QuandoCamposObrigatoriosFaltam() throws Exception {
        UserCreateDto invalid = new UserCreateDto();
        invalid.setName("");
        invalid.setEmail("nao-e-um-email");
        // role deliberadamente nula

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_deveRetornar409QuandoEmailJaExiste() throws Exception {
        UserCreateDto request = new UserCreateDto();
        request.setName("Joao");
        request.setEmail("joao@helpdesk.com");
        request.setRole(UserRole.ADMIN);

        when(userService.createUser(any(UserCreateDto.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists"));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void findById_deveRetornar404QuandoNaoEncontrado() throws Exception {
        when(userService.findUserById(eq(404L)))
                .thenThrow(new EntityNotFoundException("User not found"));

        mockMvc.perform(get("/api/users/404"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listAllUsers_deveRetornar200ComLista() throws Exception {
        UserResponseDto user = new UserResponseDto();
        user.setId(1L);
        user.setName("Ana");
        user.setRole(UserRole.TECHNICIAN);
        user.setActive(true);

        when(userService.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Ana"));
    }

    @Test
    void deleteUser_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void reactivateUser_deveRetornar200ComUsuarioAtivo() throws Exception {
        UserResponseDto response = new UserResponseDto();
        response.setId(3L);
        response.setActive(true);

        when(userService.reActiveUser(3L)).thenReturn(response);

        mockMvc.perform(post("/api/users/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));
    }
}
