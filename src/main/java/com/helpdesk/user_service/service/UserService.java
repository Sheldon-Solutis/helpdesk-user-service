package com.helpdesk.user_service.service;

import com.helpdesk.user_service.dto.UserCreateDto;
import com.helpdesk.user_service.dto.UserUpdateDto;
import com.helpdesk.user_service.dto.UserResponseDto;
import com.helpdesk.user_service.model.User;
import com.helpdesk.user_service.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

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

    public UserResponseDto updateUser(Long id, UserUpdateDto dto){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if(dto.getName() != null && !dto.getName().isBlank()){
            user.setName(dto.getName());
        }
        if(dto.getEmail() != null && !dto.getEmail().isBlank()){
            String newEmail = dto.getEmail();

            if (!newEmail.equalsIgnoreCase(user.getEmail()))
                if (userRepository.existsByEmailAndIdNot(newEmail, id))
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "Email already exists");

            user.setEmail(dto.getEmail());
        }

        userRepository.save(user);

        return new UserResponseDto(user);
    }

    public UserResponseDto createUser(@RequestBody UserCreateDto user) {
        User newUser = new User();
        String email = user.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists"
            );
        }

        newUser.setEmail(email);
        newUser.setName(user.getName());
        newUser.setRole(user.getRole());
        userRepository.save(newUser);

        return new UserResponseDto(newUser);
    }

    public UserResponseDto findUserById(Long id){
        User user = userRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "User not found with id: " + id));

        return new UserResponseDto(user);
    }

    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "User not found with id: " + id));

        user.setActive(false);
        userRepository.save(user);
    }

    public UserResponseDto reActiveUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "User not found with id: " + id));

        user.setActive(true);
        userRepository.save(user);

        return new UserResponseDto(user);
    }
}
