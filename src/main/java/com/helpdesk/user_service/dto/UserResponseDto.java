package com.helpdesk.user_service.dto;

import com.helpdesk.user_service.enums.UserRole;

import lombok.*;

@Getter
@Setter
public class UserResponseDto {
    private String name;
    private String email;
    private UserRole role;
}
