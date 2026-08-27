package com.helpdesk.user_service.service;

import com.helpdesk.user_service.dto.UserCreateDto;
import com.helpdesk.user_service.dto.UserResponseDto;
import com.helpdesk.user_service.model.User;
import com.helpdesk.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponseDto> findAll() {
        return userRepository.findAllByActiveTrue()
                .stream()
                .map(UserResponseDto::new)
                .toList();
    }

    public UserResponseDto createUser(@RequestBody UserCreateDto user) {
        User newUser = new User();

        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        newUser.setRole(user.getRole());
        userRepository.save(newUser);

        return new UserResponseDto(newUser);
    }

    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email);

        user.setActive(false);
        userRepository.save(user);
    }

    public UserResponseDto reActiveUser(String email){
        User user = userRepository.findByEmail(email);

        user.setActive(true);
        userRepository.save(user);

        return new UserResponseDto(user);
    }
}
