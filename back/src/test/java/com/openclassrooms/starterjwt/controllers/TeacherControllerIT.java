package com.openclassrooms.starterjwt.controllers;

import java.util.List;

import org.hamcrest.CoreMatchers;
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

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.controllers.TeacherController;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = TeacherController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TeacherControllerIT {
    
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private TeacherService teacherService;
    @Mock
    private TeacherMapper teacherMapper;
    
    Long teacherId = 1L;
    Teacher teacher;
    TeacherDto teacherDto = new TeacherDto();

    @BeforeAll
    public void init() {
        
        teacher = Teacher.builder()
            .id(teacherId)
            .lastName("The")
            .firstName("Professor")
            .build();

        teacherDto.setId(teacherId);
        teacherDto.setLastName("The");
        teacherDto.setFirstName("Professor");
    }

    @Test
    public void findById_shouldReturn_teacherDto() throws Exception {
        
        when(teacherService.findById(teacher.getId())).thenReturn(teacher);

        ResultActions response = mockMvc.perform(
            get("/api/teacher")
                .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(teacherMapper.toDto(teacher).toString())
        );

        response
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(teacherMapper.toDto(teacher))));

        verify(teacherService).findById(teacher.getId());
        verify(teacher);
    }

    @Test
    public void findAll_shouldReturn_teachersList() throws Exception {

        List<Teacher> teachers = teacherService.findAll();

        when(teacherService.findAll()).thenReturn(teachers);

        ResultActions response = mockMvc.perform(get("/api/teacher")
            .contentType(MediaType.APPLICATION_JSON)
        );

        response
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(teacherMapper.toDto(teachers))));
  
        // return ResponseEntity.ok().body(this.teacherMapper.toDto(teachers));

        verify(teacherService).findAll();
        verify(teachers);
    }
}
