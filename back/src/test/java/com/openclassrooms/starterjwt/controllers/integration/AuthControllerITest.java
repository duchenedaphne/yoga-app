package com.openclassrooms.starterjwt.controllers.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerITest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void login_shouldReturn_ok() throws Exception {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");

        mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(loginRequest))
        )
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void register_withExistingEmail_shouldReturn_badRequest() throws Exception {

        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("yoga@studio.com");
        signUpRequest.setPassword("test!1234");
        signUpRequest.setFirstName("new");
        signUpRequest.setLastName("user");

        mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(signUpRequest))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
