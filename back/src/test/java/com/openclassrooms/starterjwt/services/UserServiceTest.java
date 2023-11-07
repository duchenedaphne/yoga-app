package com.openclassrooms.starterjwt.services;

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

        Assertions.assertThat(savedUser).isNotNull();
    }
    
    @Test
    public void delete_shouldReturn_void() {
        
        doNothing().when(userRepository).deleteById(user.getId());

        userService.delete(user.getId());

        verify(userRepository, times(1)).deleteById(user.getId());
        
        assertAll(() -> userService.delete(user.getId()));
    }
}
