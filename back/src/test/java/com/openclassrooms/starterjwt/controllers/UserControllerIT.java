package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import com.openclassrooms.starterjwt.controllers.UserController;

import org.hamcrest.CoreMatchers;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.assertj.core.api.Assertions;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerIT {
    
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;

    Long userId = 1L;
    User user;
    UserDto userDto = new UserDto();

    @BeforeAll
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
    public void findById_shouldReturn_userDto() throws Exception {

        when(userService.findById(user.getId())).thenReturn(user);

        ResultActions response = mockMvc.perform(
            get("/api/user/{id}")
                .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userMapper.toDto(user).toString())
        );

        response
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(userMapper.toDto(user))));
    }

    @Test
    public void findById_withWrongId_shouldReturn_notFound() throws Exception {

        doNothing().when(userService.findById(3L));

        ResultActions response = mockMvc.perform(
            get("/api/user/{id}")
                .param("id", "3")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void delete_shouldReturn_void() throws Exception {

        doNothing().when(userService).delete(user.getId());

        ResultActions response = mockMvc.perform(
            delete("/api/user/{id}")
                .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
