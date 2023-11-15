package com.openclassrooms.starterjwt.controllers.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TeacherControllerITest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void findAll_teachers() throws Exception {
        
        mockMvc.perform(MockMvcRequestBuilders
            .get("/api/teacher"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    public void findById_teacher() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
            .get("/api/teacher/1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
        
        mockMvc.perform(MockMvcRequestBuilders
            .get("/api/teacher/50")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
        
        mockMvc.perform(MockMvcRequestBuilders
            .get("/api/teacher/NaN")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
