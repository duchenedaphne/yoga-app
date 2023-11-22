package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;

import com.openclassrooms.starterjwt.controllers.UserController;

import org.assertj.core.api.Assertions;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import org.springframework.security.core.AuthenticationException;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Autowired
    private AuthenticationException authException;
    
    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private AuthEntryPointJwt authEntryPointJwt;

    @InjectMocks
    private UserController userController;

    Long userId = 1L;
    User user;
    UserDto userDto = new UserDto();

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

        userDto.setId(userId);
        userDto.setLastName("Basic");
        userDto.setFirstName("User");
        userDto.setEmail("test@email.com");
        userDto.setPassword("password");
        userDto.setAdmin(false);
    }

    @Test
    public void findById_shouldReturn_userDto() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(userService.findById(user.getId())).thenReturn(user);

        ResponseEntity<?> responseEntity = userController.findById(Long.toString(user.getId()));
        
        verify(userService, times(1)).findById(user.getId());
        
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(userMapper.toDto(user));
    }
    
    @Test
    public void findById_with_userNull_shouldReturn_notFound() {
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(userService.findById(user.getId())).thenReturn(null);

        ResponseEntity<?> responseEntity = userController.findById(Long.toString(user.getId()));
        
        verify(userService, times(1)).findById(user.getId());
        
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
    }
    
    @Test
    public void findById_with_wrongId_shouldReturn_badRequest() {
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        ResponseEntity<?> responseEntity = userController.findById("Id");
        
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
    }
    
    @Test
    public void save_with_userNull_shouldReturn_notFound() {
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(userService.findById(user.getId())).thenReturn(null);

        ResponseEntity<?> responseEntity = userController.save(Long.toString(user.getId()));
        
        verify(userService, times(1)).findById(user.getId());
        
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
    }
    
    @Test
    public void save_with_wrongId_shouldReturn_badRequest() {
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        ResponseEntity<?> responseEntity = userController.save("Id");
        
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
    }
    
    @Test
    public void save_shouldReturn_ok() {
    
        UserDetails userDetails = new UserDetailsImpl(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.isAdmin(), user.getPassword());

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        String jwts = "token";

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(jwtUtils.generateJwtToken(authentication)).thenReturn(jwts);

        jwtUtils.generateJwtToken(authentication);

        when(userService.findById(user.getId())).thenReturn(user);
        doNothing().when(userService).delete(user.getId());

        ResponseEntity<?> responseEntity = userController.save("1");

        verify(userService, times(1)).delete(user.getId());
        
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }
    
    @Test
    public void save_shouldReturn_unauthorized() throws IOException, ServletException {

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            "random@test.com", "password", authorities
        );

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null);
        
        SecurityContextHolder.getContext().setAuthentication(token);
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request, response));

        when(userService.findById(user.getId())).thenReturn(user);
        doNothing().when(authEntryPointJwt).commence(request, response, authException);

        ResponseEntity<?> responseEntity = userController.save("1");

        authEntryPointJwt.commence(request, response, authException);
        
        assertAll(() -> authEntryPointJwt.commence(request, response, authException));
        
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(401);
    }
}
