package com.openclassrooms.starterjwt.services;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    Long userId = 1L;
    User user;

    @BeforeEach
    public void init() {
        user = User.builder()
            .id(userId)
            .createdAt(LocalDate.of(2023, Month.NOVEMBER, 10).atStartOfDay())
            .updatedAt(LocalDate.of(2023, Month.NOVEMBER, 10).atStartOfDay())
            .lastName("Basic")
            .firstName("User")
            .email("test@email.com")
            .password("password")
            .admin(false)
            .build();
    }
    
    @Test
    public void findById_shouldReturn_user() {
            
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        
        User savedUser = userService.findById(user.getId());
        
        verify(userRepository).findById(user.getId());

        Assertions.assertThat(savedUser).isEqualTo(user);
        Assertions.assertThat(savedUser.toString()).isEqualTo(user.toString());
        Assertions.assertThat(user.toString()).isEqualTo(savedUser.toString());
        Assertions.assertThat(savedUser.getId()).isEqualTo(user.getId());
        Assertions.assertThat(savedUser.getLastName()).isEqualTo(user.getLastName());
        Assertions.assertThat(savedUser.getFirstName()).isEqualTo(user.getFirstName());
        Assertions.assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(savedUser.getPassword()).isEqualTo(user.getPassword());
        Assertions.assertThat(savedUser.isAdmin()).isEqualTo(user.isAdmin());
        Assertions.assertThat(savedUser.getCreatedAt()).isEqualTo(user.getCreatedAt());
        Assertions.assertThat(savedUser.getUpdatedAt()).isEqualTo(user.getUpdatedAt());
    }
    
    @Test
    public void delete_shouldReturn_void() {
        
        doNothing().when(userRepository).deleteById(user.getId());

        userService.delete(user.getId());

        verify(userRepository, times(1)).deleteById(user.getId());
        
        assertAll(() -> userService.delete(user.getId()));
    }
}
