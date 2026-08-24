package main.java.com.helpdesk.user_service.dto;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
public class UserDto {
    private String name;
    private String email;
    private UserRole role;
}
