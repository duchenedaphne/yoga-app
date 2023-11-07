package com.openclassrooms.starterjwt.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.SessionRepository;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ExtendWith(MockitoExtension.class)
public class SessionRepositoryIT {

    @Mock
    private TeacherRepository teacherRepository;
    
    @InjectMocks
    private SessionRepository sessionRepository;

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
        teacherRepository.save(teacher);
        
        session = Session.builder()
            .id(sessionId)
            .name("Beginners")
            .date(new Date())
            .description("yoga test")
            .teacher(teacher)
            .build();
    }

    @Test
    public void save_shouldReturn_session() {
        
        Session savedSession = sessionRepository.save(session);
        
        Assertions.assertThat(savedSession).isNotNull();
        Assertions.assertThat(savedSession.getId()).isGreaterThan(0);
    }

    @Test
    public void findAll_shouldReturn_allSessions() {
        
        Long teacherId2 = 2L;
        Teacher teacher2 = Teacher.builder()
            .id(teacherId2)
            .lastName("The")
            .firstName("Professor")
            .build();
        teacherRepository.save(teacher2);

        Long sessionId2 = 2L;
        Session session2 = Session.builder()
            .id(sessionId2)
            .name("Advanced")
            .date(new Date())
            .description("yoga")
            .teacher(teacher2)
            .build();

        sessionRepository.save(session);
        sessionRepository.save(session2);
        
        List<Session> sessions = sessionRepository.findAll();
        
        Assertions.assertThat(sessions).isNotNull();
        Assertions.assertThat(sessions.size()).isGreaterThan(0);
    }
    
    @Test
    public void findById_shouldReturn_session() {
        
        sessionRepository.save(session);
        
        Session searchedSession = sessionRepository.findById(session.getId()).get();
        
        Assertions.assertThat(searchedSession).isNotNull();
    }

    @Test
    public void update_shouldReturn_session() {
        
        sessionRepository.save(session);
        
        Session newSession = sessionRepository.findById(session.getId()).get();
        newSession.setName("one");
        newSession.setDescription("last session");

        Session updatedSession = sessionRepository.save(newSession);
        
        Assertions.assertThat(updatedSession.getName()).isNotNull();
        Assertions.assertThat(updatedSession.getDescription()).isNotNull();
    }

    @Test
    public void delete_shouldReturn_emptySession() {
        
        sessionRepository.save(session);
        
        sessionRepository.deleteById(session.getId());
        Optional<Session> returnedSession = sessionRepository.findById(session.getId());
        
        Assertions.assertThat(returnedSession).isEmpty();
    }
}
