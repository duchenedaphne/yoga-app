package com.openclassrooms.starterjwt.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    Long teacherId = 1L;
    Teacher teacher;

    @BeforeEach
    public void init() {
        teacher = Teacher.builder()
            .id(teacherId)
            .lastName("The")
            .firstName("Professor")
            .build();
    }
    
    @Test
    public void findAll_shouldReturn_teachersList() {
        
        Long teacherId2 = 2L;
        Teacher teacher2 = Teacher.builder()
            .id(teacherId2)
            .lastName("The")
            .firstName("Professor")
            .build();

        when(teacherRepository.findAll()).thenReturn(Arrays.asList(teacher, teacher2));

        List<Teacher> teachersList = teacherService.findAll();
        
        verify(teacherRepository).findAll();

        Assertions.assertThat(teachersList).isNotNull();
    }
    
    @Test
    public void findById_shouldReturn_teacher() {
            
        when(teacherRepository.findById(teacher.getId())).thenReturn(Optional.ofNullable(teacher));
        
        Teacher savedTeacher = teacherService.findById(teacher.getId());

        verify(teacherRepository).findById(teacher.getId());

        Assertions.assertThat(savedTeacher).isNotNull();
    }
}
