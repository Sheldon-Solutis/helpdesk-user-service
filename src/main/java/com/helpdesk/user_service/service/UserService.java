package com.helpdesk.user_service.service;

import com.helpdesk.user_service.dto.UserCreateDto;
import com.helpdesk.user_service.dto.UserResponseDto;
import com.helpdesk.user_service.model.User;
import com.helpdesk.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponseDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map( user -> {
                    UserResponseDto dto = new UserResponseDto();
                    dto.setName(user.getName());
                    dto.setEmail(user.getEmail());
                    dto.setRole(user.getRole());

                    return dto;
                })
                .toList();
    }

    public UserResponseDto createUser(@RequestBody UserCreateDto user) {
        User userEntity = new User();

        userEntity.setEmail(user.getEmail());
        userEntity.setName(user.getName());
        userEntity.setRole(user.getRole());
        userEntity.setCreatedAt(new Date());
        userEntity.setActive(true);

        User savedUser = userRepository.save(userEntity);

        UserResponseDto response = new UserResponseDto();
        response.setEmail(savedUser.getEmail());
        response.setName(savedUser.getName());
        response.setRole(savedUser.getRole());
        return response;
    }

    public UserResponseDto deleteUser(String email) {
        User user = userRepository.findByEmailActiveTrue(email);
        user.setActive(false);
        return new UserResponseDto(user);
    }
}
