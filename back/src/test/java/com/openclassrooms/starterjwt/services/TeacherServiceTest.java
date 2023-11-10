package com.openclassrooms.starterjwt.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
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
            .createdAt(LocalDate.of(2023, Month.NOVEMBER, 10).atStartOfDay())
            .updatedAt(LocalDate.of(2023, Month.NOVEMBER, 10).atStartOfDay())
            .lastName("The")
            .firstName("Professor")
            .build();
    }
    
    @Test
    public void findAll_shouldReturn_teachersList() {
        
        Long teacherId2 = 2L;
        Teacher teacher2 = new Teacher();
        teacher2.setId(teacherId2);
        teacher2.setLastName("Second");
        teacher2.setFirstName("Teacher");
        teacher2.setCreatedAt(LocalDate.of(2023, Month.NOVEMBER, 10).atStartOfDay());
        teacher2.setUpdatedAt(LocalDate.of(2023, Month.NOVEMBER, 10).atStartOfDay());

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

        Assertions.assertThat(savedTeacher).isEqualTo(teacher);
        Assertions.assertThat(savedTeacher.toString()).isEqualTo(teacher.toString());
        Assertions.assertThat(teacher.toString()).isEqualTo(savedTeacher.toString());
        Assertions.assertThat(savedTeacher.getId()).isEqualTo(teacher.getId());
        Assertions.assertThat(savedTeacher.getFirstName()).isEqualTo(teacher.getFirstName());
        Assertions.assertThat(savedTeacher.getLastName()).isEqualTo(teacher.getLastName());
        Assertions.assertThat(savedTeacher.getCreatedAt()).isEqualTo(teacher.getCreatedAt());
        Assertions.assertThat(savedTeacher.getUpdatedAt()).isEqualTo(teacher.getUpdatedAt());
    }
}
