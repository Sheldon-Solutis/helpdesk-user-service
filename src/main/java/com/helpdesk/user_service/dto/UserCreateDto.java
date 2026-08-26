package com.helpdesk.user_service.dto;

import com.helpdesk.user_service.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDto {
    private String name;
    private String email;
    private UserRole role;
}
