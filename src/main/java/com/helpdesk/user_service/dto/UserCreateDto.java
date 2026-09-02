package com.helpdesk.user_service.dto;

import com.helpdesk.user_service.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDto {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private UserRole role;
}
