package com.openclassrooms.starterjwt.controllers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    @Test
    public void register_shouldReturn_ok() {

        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("auth@test.com");
        signUpRequest.setPassword("password");
        signUpRequest.setLastName("test");
        signUpRequest.setFirstName("test");

        User user = new User();

        MockHttpServletRequest request = new MockHttpServletRequest();
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(passwordEncoder.encode(signUpRequest.getPassword())).thenReturn("passwordEncoded");
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        ResponseEntity<?> responseEntity = authController.registerUser(signUpRequest);
        
        verify(userRepository, times(1)).save(user);
        
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }
}
