package com.openclassrooms.starterjwt.services;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock 
    private TeacherService teacherService;

    @InjectMocks
    private SessionService sessionService;
    
    Long teacherId = 1L;
    Teacher teacher;
    Long sessionId = 1L;
    Session session;

    @BeforeEach
    public void init() {
        teacher = Teacher.builder()
            .id(teacherId)
            .lastName("The")
            .firstName("Professor")
            .build();
            
        session = Session.builder()
            .id(sessionId)
            .name("Beginners")
            .date(new Date())
            .description("yoga test")
            .teacher(teacher)
            .build();
    }

    @Test
    public void findAll_shouldReturn_sessionsList() {
        
        Long teacherId2 = 2L;
        Teacher teacher2 = Teacher.builder()
            .id(teacherId2)
            .lastName("Second")
            .firstName("Teacher")
            .build();
            
        Long sessionId = 2L;
        Session session2 = Session.builder()
            .id(sessionId)
            .name("Beginners")
            .date(new Date())
            .description("yoga test")
            .teacher(teacher2)
            .build();
        
        when(sessionRepository.findAll()).thenReturn(Arrays.asList(session, session2));

        List<Session> sessionsList = sessionService.findAll();
        
        verify(sessionRepository).findAll();

        Assertions.assertThat(sessionsList).isNotNull();
    }

    @Test 
    public void create_shouldReturn_session() {

        when(sessionRepository.save(Mockito.any(Session.class))).thenReturn(session);

        Session savedSession = sessionService.create(session);  

        verify(sessionRepository).save(session);

        Assertions.assertThat(savedSession).isNotNull();
    }

    @Test 
    public void update_shouldReturn_session() {

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.ofNullable(session));

        Session newSession = sessionService.getById(session.getId());
        newSession.setName("Session test");
        newSession.setDescription("Level test");

        when(sessionRepository.save(Mockito.any(Session.class))).thenReturn(newSession);
        Session updatedSession = sessionService.update(session.getId(), newSession);

        verify(sessionRepository).findById(session.getId());
        verify(sessionRepository).save(newSession);

        Assertions.assertThat(updatedSession).isNotNull();
    }
    
    @Test
    public void getById_shouldReturn_session() {
            
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.ofNullable(session));
        
        Session savedSession = sessionService.getById(session.getId());
        
        verify(sessionRepository).findById(session.getId());

        Assertions.assertThat(savedSession).isNotNull();
    }
    
    @Test
    public void delete_shouldReturn_void() {

        doNothing().when(sessionRepository).deleteById(session.getId());

        sessionService.delete(session.getId());

        verify(sessionRepository, times(1)).deleteById(session.getId());

        assertAll(() -> sessionService.delete(session.getId()));
    }    
}
