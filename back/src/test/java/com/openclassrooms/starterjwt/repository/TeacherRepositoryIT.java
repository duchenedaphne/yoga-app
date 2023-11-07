package com.openclassrooms.starterjwt.repository;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ExtendWith(MockitoExtension.class)
public class TeacherRepositoryIT {
    
    @InjectMocks
    private TeacherRepository teacherRepository;

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
    public void save_shouldReturn_teacher() {
        
        Teacher savedTeacher = teacherRepository.save(teacher);
        
        Assertions.assertThat(savedTeacher).isNotNull();
        Assertions.assertThat(savedTeacher.getId()).isGreaterThan(0);
    }

    @Test
    public void findAll_shouldReturn_allTeachers() {
        
        Long teacherId2 = 2L;
        Teacher teacher2 = Teacher.builder()
            .id(teacherId2)
            .lastName("Second")
            .firstName("Teacher")
            .build();

        teacherRepository.save(teacher);
        teacherRepository.save(teacher2);
        
        List<Teacher> teachers = teacherRepository.findAll();
        
        Assertions.assertThat(teachers).isNotNull();
        Assertions.assertThat(teachers.size()).isGreaterThan(0);
    }
    
    @Test
    public void findById_shouldReturn_teacher() {
        
        teacherRepository.save(teacher);
        
        Teacher searchedTeacher = teacherRepository.findById(teacher.getId()).get();
        
        Assertions.assertThat(searchedTeacher).isNotNull();
    }

    @Test
    public void update_shouldReturn_teacher() {
        
        teacherRepository.save(teacher);
        
        Teacher newTeacher = teacherRepository.findById(teacher.getId()).get();
        newTeacher.setFirstName("one");
        newTeacher.setLastName("last");

        Teacher updatedTeacher = teacherRepository.save(newTeacher);
        
        Assertions.assertThat(updatedTeacher.getFirstName()).isNotNull();
        Assertions.assertThat(updatedTeacher.getLastName()).isNotNull();
    }

    @Test
    public void delete_shouldReturn_emptyTeacher() {
        
        teacherRepository.save(teacher);
        
        teacherRepository.deleteById(teacher.getId());
        Optional<Teacher> returnedTeacher = teacherRepository.findById(teacher.getId());
        
        Assertions.assertThat(returnedTeacher).isEmpty();
    }
}
