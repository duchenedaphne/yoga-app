package com.openclassrooms.starterjwt.controllers.integration;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class SessionControllerITest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void findAll_sessions() throws Exception {
        
        mockMvc.perform(MockMvcRequestBuilders
            .get("/api/session"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void update_session() throws Exception {
        
        SessionDto sessionDtoUpdate = new SessionDto();
        sessionDtoUpdate.setDate(new Date());
        sessionDtoUpdate.setDescription("updated test");

        mockMvc.perform(MockMvcRequestBuilders
            .put("/api/session/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(sessionDtoUpdate)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void findById_session() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
            .get("/api/session/1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
        
        mockMvc.perform(MockMvcRequestBuilders
            .get("/api/session/50")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
        
        mockMvc.perform(MockMvcRequestBuilders
            .get("/api/session/NaN")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void participate_unparticipate() throws Exception {
            
        mockMvc.perform(MockMvcRequestBuilders
            .post("/api/session/1/participate/2")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
            
        mockMvc.perform(MockMvcRequestBuilders
            .post("/api/session/NaN/participate/NaN")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
        
        mockMvc.perform(MockMvcRequestBuilders
            .delete("/api/session/1/participate/2")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
        
        mockMvc.perform(MockMvcRequestBuilders
            .delete("/api/session/NaN/participate/NaN")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void delete_session() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
            .delete("/api/session/50")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
        
        mockMvc.perform(MockMvcRequestBuilders
            .delete("/api/session/NaN")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
