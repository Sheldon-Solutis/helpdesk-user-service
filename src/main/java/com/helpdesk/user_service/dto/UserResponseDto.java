package com.helpdesk.user_service.dto;

import com.helpdesk.user_service.enums.UserRole;

import com.helpdesk.user_service.model.User;
import lombok.*;

@Getter
@Setter
@ToString
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private UserRole role;
    private boolean active;

    public UserResponseDto(){}

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.active = user.isActive();
    }
}
