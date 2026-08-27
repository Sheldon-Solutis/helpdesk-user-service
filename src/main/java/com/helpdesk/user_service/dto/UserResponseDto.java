package com.helpdesk.user_service.dto;

import com.helpdesk.user_service.enums.UserRole;

import com.helpdesk.user_service.model.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class UserResponseDto {
    private String name;
    private String email;
    private UserRole role;

    public UserResponseDto(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}
