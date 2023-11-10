package com.openclassrooms.starterjwt.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.services.UserService;
import com.openclassrooms.starterjwt.controllers.AuthController;

import org.hamcrest.CoreMatchers;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AuthControllerIT {
    
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;

    // @Test
    public void login_shouldReturn_jwtResponse() throws Exception {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("auth@test.com");
        loginRequest.setPassword("password");

        given(authenticationManager.authenticate(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        
        // when(userService.findById(user.getId())).thenReturn(user);

        ResultActions response = mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response
            .andExpect(MockMvcResultMatchers.status().isOk());
            // .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(userMapper.toDto(user))))
    }

    @Test
    public void register_shouldReturn_okStatus() throws Exception {

        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("auth@test.com");
        signUpRequest.setPassword("password");
        signUpRequest.setLastName("test");
        signUpRequest.setFirstName("test");

        User user = new User(
            signUpRequest.getEmail(),
            signUpRequest.getLastName(),
            signUpRequest.getFirstName(),
            passwordEncoder.encode(signUpRequest.getPassword()
            ),
            false
        );
        
        when(userRepository.save(user)).thenReturn(user);

        ResultActions response = mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk());
        
        verify(userRepository).save(user);
        verify(user);
    }

    @Test
    public void register_withExistingEmail_shouldReturn_badRequest() throws Exception {

        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("auth@test.com");
        signUpRequest.setPassword("password");
        
        doNothing().when(userRepository).existsByEmail(signUpRequest.getEmail());

        ResultActions response = mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk());
        
        verify(userRepository).existsByEmail(signUpRequest.getEmail());
    }
}
