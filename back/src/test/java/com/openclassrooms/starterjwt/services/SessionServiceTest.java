package com.openclassrooms.starterjwt.services;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
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

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
            .createdAt(LocalDate.of(2023, Month.NOVEMBER, 10).atStartOfDay())
            .updatedAt(LocalDate.of(2023, Month.NOVEMBER, 10).atStartOfDay())
            .name("Beginners")
            .date(new Date())
            .description("yoga test")
            .teacher(teacher)
            .build();
    }

    @Test
    public void findAll_shouldReturn_sessionsList() {
        
        Long teacherId2 = 2L;
        Teacher teacher2 = new Teacher();
        teacher2.setId(teacherId2);
        teacher2.setLastName("Second");
        teacher2.setFirstName("Teacher");
        teacher2.setCreatedAt(LocalDate.of(2023, Month.NOVEMBER, 10).atStartOfDay());
        teacher2.setUpdatedAt(LocalDate.of(2023, Month.NOVEMBER, 10).atStartOfDay());
            
        Long sessionId = 2L;
        Session session2 = new Session();
        session2.setId(sessionId);
        session2.setName("Beginners");
        session2.setDate(new Date());
        session2.setDescription("yoga test");
        session2.setTeacher(teacher2);
        session2.setCreatedAt(LocalDate.of(2023, Month.NOVEMBER, 9).atStartOfDay());
        session2.setUpdatedAt(LocalDate.of(2023, Month.NOVEMBER, 9).atStartOfDay());
        
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

        Assertions.assertThat(savedSession).isEqualTo(session);
        Assertions.assertThat(savedSession.toString()).isEqualTo(session.toString());
        Assertions.assertThat(session.toString()).isEqualTo(savedSession.toString());
        Assertions.assertThat(session.getUsers()).isEqualTo(savedSession.getUsers());
        Assertions.assertThat(savedSession.getId()).isEqualTo(session.getId());
        Assertions.assertThat(savedSession.getName()).isEqualTo(session.getName());
        Assertions.assertThat(savedSession.getDate()).isEqualTo(session.getDate());
        Assertions.assertThat(savedSession.getDescription()).isEqualTo(session.getDescription());
        Assertions.assertThat(savedSession.getTeacher()).isEqualTo(session.getTeacher());
        Assertions.assertThat(savedSession.getCreatedAt()).isEqualTo(session.getCreatedAt());
        Assertions.assertThat(savedSession.getUpdatedAt()).isEqualTo(session.getUpdatedAt());
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

    @Test
    public void participate_shouldReturn_void() {

        Long userId = 1L;
        User user = User.builder()
            .id(userId)
            .lastName("Basic")
            .firstName("User")
            .email("test@email.com")
            .password("password")
            .admin(false)
            .build();

        List<User> users = new ArrayList<User>(Arrays.asList());
        session.setUsers(users);
            
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.ofNullable(session));
        
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        
        when(sessionRepository.save(Mockito.any(Session.class))).thenReturn(session);
        
        assertAll(() -> sessionService.participate(session.getId(), user.getId()));

        verify(sessionRepository).findById(session.getId());
        verify(userRepository).findById(user.getId()); 
        verify(sessionRepository).save(session);
    }

    @Test
    public void participate_withNullParams_shouldThrow_exception() {

        Long userId = 1L;
        User user = User.builder()
            .id(userId)
            .lastName("Basic")
            .firstName("User")
            .email("test@email.com")
            .password("password")
            .admin(false)
            .build();

        List<User> users = new ArrayList<User>(Arrays.asList());
        session.setUsers(users);
            
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.ofNullable(session));
        
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(NotFoundException.class, () -> sessionService.participate(session.getId(), user.getId()));

        verify(sessionRepository).findById(session.getId());
        verify(userRepository).findById(user.getId());

        Assertions.assertThat(exception).isNotNull();
    }

    @Test
    public void participate_withTrue_alreadyParticipate_shouldThrow_exception() {

        Long userId = 1L;
        User user = User.builder()
            .id(userId)
            .lastName("Basic")
            .firstName("User")
            .email("test@email.com")
            .password("password")
            .admin(false)
            .build();

        List<User> users = new ArrayList<User>(Arrays.asList(user));
        session.setUsers(users);
            
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.ofNullable(session));
        
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));

        Exception exception = assertThrows(BadRequestException.class, () -> sessionService.participate(session.getId(), user.getId()));

        verify(sessionRepository).findById(session.getId());
        verify(userRepository).findById(user.getId()); 

        Assertions.assertThat(exception).isNotNull();
    }
   
    @Test 
    public void noLongerParticipate_shouldReturn_void() {
        
        Long userId = 1L;
        User user = User.builder()
            .id(userId)
            .lastName("Basic")
            .firstName("User")
            .email("test@email.com")
            .password("password")
            .admin(false)
            .build();

        User fakeUser = new User();
        fakeUser.setId(2L);
        fakeUser.setLastName("Fake");
        fakeUser.setFirstName("User");
        fakeUser.setEmail("fake@email.com");
        fakeUser.setPassword("password");
        fakeUser.setAdmin(false);
        fakeUser.setCreatedAt(LocalDate.of(2023, Month.NOVEMBER, 10).atStartOfDay());
        fakeUser.setUpdatedAt(LocalDate.of(2023, Month.NOVEMBER, 10).atStartOfDay());

        List<User> users = new ArrayList<>(Arrays.asList(fakeUser, user));
        session.setUsers(users);

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.ofNullable(session));

        when(sessionRepository.save(Mockito.any(Session.class))).thenReturn(session);
        
        assertAll(() -> sessionService.noLongerParticipate(session.getId(), user.getId()));

        verify(sessionRepository).findById(session.getId());
        verify(sessionRepository).save(session);
    }

    @Test
    public void noLongerparticipate_withFalse_alreadyParticipate_shouldThrow_exception() {

        Long userId = 1L;
        User user = User.builder()
            .id(userId)
            .lastName("Basic")
            .firstName("User")
            .email("test@email.com")
            .password("password")
            .admin(false)
            .build();

        List<User> users = new ArrayList<User>(Arrays.asList());
        session.setUsers(users);
            
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.ofNullable(session));

        Exception exception = assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(session.getId(), user.getId()));

        verify(sessionRepository).findById(session.getId());

        Assertions.assertThat(exception).isNotNull();
    }

    @Test
    public void noLongerParticipate_withNullParams_shouldThrow_exception() {

        Long userId = 1L;
        User user = User.builder()
            .id(userId)
            .lastName("Basic")
            .firstName("User")
            .email("test@email.com")
            .password("password")
            .admin(false)
            .build();

        List<User> users = new ArrayList<User>(Arrays.asList(user));
        session.setUsers(users);
            
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(session.getId(), user.getId()));

        verify(sessionRepository).findById(session.getId());

        Assertions.assertThat(exception).isNotNull();
    }
}
