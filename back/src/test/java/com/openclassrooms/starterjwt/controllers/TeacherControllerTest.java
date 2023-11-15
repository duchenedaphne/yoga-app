package com.openclassrooms.starterjwt.controllers;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.controllers.TeacherController;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {
    
    @Mock
    private TeacherService teacherService;
    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;
    
    Long teacherId = 1L;
    Teacher teacher;
    TeacherDto teacherDto = new TeacherDto();
    List<Teacher> teachers = new ArrayList<Teacher>(Arrays.asList());

    @BeforeEach
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
    public void findById_shouldReturn_teacherDto() {
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(teacherService.findById(teacher.getId())).thenReturn(teacher);

        ResponseEntity<?> responseEntity = teacherController.findById(Long.toString(teacher.getId()));
        
        verify(teacherService, times(1)).findById(teacher.getId());
        
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(teacherMapper.toDto(teacher));
    }
    
    @Test
    public void findById_with_teacherNull_shouldReturn_notFound() {
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(teacherService.findById(teacher.getId())).thenReturn(null);

        ResponseEntity<?> responseEntity = teacherController.findById(Long.toString(teacher.getId()));
        
        verify(teacherService, times(1)).findById(teacher.getId());
        
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
    }
    
    @Test
    public void findById_with_wrongId_shouldReturn_badRequest() {
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        ResponseEntity<?> responseEntity = teacherController.findById("Id");
        
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void findAll_shouldReturn_teachersListToDto() {
        
        Long teacherId2 = 2L;
        Teacher teacher2 = new Teacher();
        teacher2.setId(teacherId2);
        teacher2.setLastName("Second");
        teacher2.setFirstName("Teacher");
        teacher2.setCreatedAt(LocalDate.of(2023, Month.NOVEMBER, 10).atStartOfDay());
        teacher2.setUpdatedAt(LocalDate.of(2023, Month.NOVEMBER, 10).atStartOfDay());

        List<Teacher> teachers = Arrays.asList(teacher, teacher2);

        MockHttpServletRequest request = new MockHttpServletRequest();
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        
        when(teacherService.findAll()).thenReturn(teachers);

        ResponseEntity<?> responseEntity = teacherController.findAll();
        
        verify(teacherService, times(1)).findAll();
        
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(teacherMapper.toDto(teachers));
    }
}
