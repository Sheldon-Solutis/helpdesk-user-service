package com.helpdesk.user_service.service;

import com.helpdesk.user_service.dto.UserCreateDto;
import com.helpdesk.user_service.dto.UserResponseDto;
import com.helpdesk.user_service.dto.UserUpdateDto;
import com.helpdesk.user_service.enums.UserRole;
import com.helpdesk.user_service.model.User;
import com.helpdesk.user_service.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserCreateDto createDto;

    @BeforeEach
    void setUp() {
        createDto = new UserCreateDto();
        createDto.setName("Ana Souza");
        createDto.setEmail("ana.souza@helpdesk.com");
        createDto.setRole(UserRole.TECHNICIAN);
    }

    @Test
    void createUser_deveCriarQuandoEmailNaoExiste() {
        when(userRepository.existsByEmail(createDto.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        UserResponseDto response = userService.createUser(createDto);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getRole()).isEqualTo(UserRole.TECHNICIAN);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_deveLancarConflitoQuandoEmailJaExiste() {
        when(userRepository.existsByEmail(createDto.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(createDto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Email already exists");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findUserById_deveLancarNotFoundQuandoNaoExiste() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findUserById(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void updateUser_deveAtualizarNomeEEmailQuandoValido() {
        User existing = User.builder().id(5L).name("Carlos").email("carlos@helpdesk.com")
                .role(UserRole.CLIENT).active(true).build();
        UserUpdateDto dto = new UserUpdateDto();
        dto.setName("Carlos Eduardo");
        dto.setEmail("carlos.eduardo@helpdesk.com");

        when(userRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(userRepository.existsByEmailAndIdNot(dto.getEmail(), 5L)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserResponseDto response = userService.updateUser(5L, dto);

        assertThat(response.getName()).isEqualTo("Carlos Eduardo");
        assertThat(existing.getEmail()).isEqualTo("carlos.eduardo@helpdesk.com");
    }

    @Test
    void updateUser_deveLancarConflitoQuandoNovoEmailJaPertenceAOutroUsuario() {
        User existing = User.builder().id(5L).name("Carlos").email("carlos@helpdesk.com")
                .role(UserRole.CLIENT).active(true).build();
        UserUpdateDto dto = new UserUpdateDto();
        dto.setEmail("outro@helpdesk.com");

        when(userRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(userRepository.existsByEmailAndIdNot("outro@helpdesk.com", 5L)).thenReturn(true);

        assertThatThrownBy(() -> userService.updateUser(5L, dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Email already exists");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_naoDeveTocarEmailQuandoDtoNaoInformaEmail() {
        User existing = User.builder().id(5L).name("Carlos").email("carlos@helpdesk.com")
                .role(UserRole.CLIENT).active(true).build();
        UserUpdateDto dto = new UserUpdateDto();
        dto.setName("Carlos Novo");
        // email em branco

        when(userRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        userService.updateUser(5L, dto);

        verify(userRepository, never()).existsByEmailAndIdNot(anyString(), eq(5L));
        assertThat(existing.getEmail()).isEqualTo("carlos@helpdesk.com");
    }

    @Test
    void deleteUserById_deveInativarUsuarioSemRemoverRegistro() {
        User existing = User.builder().id(7L).name("Maria").email("maria@helpdesk.com")
                .role(UserRole.CLIENT).active(true).build();
        when(userRepository.findById(7L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        userService.deleteUserById(7L);

        assertThat(existing.isActive()).isFalse();
        verify(userRepository).save(existing);
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void reActiveUser_deveReativarUsuarioInativo() {
        User existing = User.builder().id(7L).name("Maria").email("maria@helpdesk.com")
                .role(UserRole.CLIENT).active(false).build();
        when(userRepository.findById(7L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserResponseDto response = userService.reActiveUser(7L);

        assertThat(response.isActive()).isTrue();
        assertThat(existing.isActive()).isTrue();
    }

    @Test
    void findAll_devePassarPorFindAllByActiveTrue() {
        User active = User.builder().id(1L).name("Ativo").email("ativo@helpdesk.com")
                .role(UserRole.CLIENT).active(true).build();
        when(userRepository.findAllByActiveTrue()).thenReturn(List.of(active));

        List<UserResponseDto> result = userService.findAll();

        assertThat(result).hasSize(1);
        verify(userRepository).findAllByActiveTrue();
        verify(userRepository, never()).findAll();
    }
}
