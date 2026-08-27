package com.helpdesk.user_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPatchDto {
    private String name;
    private String email;
}
