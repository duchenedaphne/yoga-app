package com.openclassrooms.starterjwt.controllers;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import com.openclassrooms.starterjwt.services.UserService;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;

@ExtendWith(MockitoExtension.class)
public class SessionControllerTest {
    
    @Mock
    private SessionService sessionService;
    @Mock
    private SessionMapper sessionMapper;
    @Mock
    private UserService userService; 

    @InjectMocks
    private SessionController sessionController;

    Long teacherId = 1L;
    Teacher teacher;
    Long sessionId = 1L;
    Session session;
    List<User> users = new ArrayList<User>(Arrays.asList());

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
            .users(users)
            .build();
    }
    
    @Test
    public void findById_shouldReturn_sessionDto() {
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(sessionService.getById(session.getId())).thenReturn(session);

        ResponseEntity<?> responseEntity = sessionController.findById(Long.toString(session.getId()));
        
        verify(sessionService, times(1)).getById(session.getId());
        
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(sessionMapper.toDto(session));
    }
    
    @Test
    public void findById_with_sessionNull_shouldReturn_notFound() {
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(sessionService.getById(session.getId())).thenReturn(null);

        ResponseEntity<?> responseEntity = sessionController.findById(Long.toString(session.getId()));
        
        verify(sessionService, times(1)).getById(session.getId());
        
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
    }
    
    @Test
    public void findById_with_wrongId_shouldReturn_badRequest() {
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        ResponseEntity<?> responseEntity = sessionController.findById("Id");
        
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void findAll_shouldReturn_sessionsListToDto() {
        
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

        List<Session> sessions = Arrays.asList(session, session2);

        MockHttpServletRequest request = new MockHttpServletRequest();
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        
        when(sessionService.findAll()).thenReturn(sessions);

        ResponseEntity<?> responseEntity = sessionController.findAll();
        
        verify(sessionService, times(1)).findAll();
        
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(sessionMapper.toDto(sessions));
    }

    @Test
    public void create_shouldReturn_sessionDto() {

        SessionDto sessionDto = sessionMapper.toDto(session);

        MockHttpServletRequest request = new MockHttpServletRequest();
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        
        when(sessionService.create(Mockito.any())).thenReturn(session);

        ResponseEntity<?> responseEntity = sessionController.create(sessionDto);
        
        verify(sessionService, times(1)).create(sessionMapper.toEntity(sessionDto));
        
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(sessionMapper.toDto(session));
    }

    @Test
    public void update_shouldReturn_sessionDto() {

        SessionDto sessionDto = sessionMapper.toDto(session);

        MockHttpServletRequest request = new MockHttpServletRequest();
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);
		when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(session.getId(), session)).thenReturn(session);

        ResponseEntity<?> responseEntity = sessionController.update(Long.toString(session.getId()), sessionDto);
        
        verify(sessionService, times(1)).update(session.getId(), sessionMapper.toEntity(sessionDto));
        
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(sessionMapper.toDto(session));
    }

    @Test
    public void update_with_wrongId_shouldReturn_badRequest() {

        SessionDto sessionDto = sessionMapper.toDto(session);

        MockHttpServletRequest request = new MockHttpServletRequest();
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        ResponseEntity<?> responseEntity = sessionController.update("Id", sessionDto);
        
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
    }
    
    @Test
    public void save_shouldReturn_ok() {
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(sessionService.getById(session.getId())).thenReturn(session);
        doNothing().when(sessionService).delete(session.getId());

        ResponseEntity<?> responseEntity = sessionController.save(Long.toString(session.getId()));
        
        verify(sessionService, times(1)).getById(session.getId());
        
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }
    
    @Test
    public void save_with_sessionNull_shouldReturn_notFound() {
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(sessionService.getById(session.getId())).thenReturn(null);

        ResponseEntity<?> responseEntity = sessionController.save(Long.toString(session.getId()));
        
        verify(sessionService, times(1)).getById(session.getId());
        
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
    }
    
    @Test
    public void save_with_wrongId_shouldReturn_badRequest() {
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        ResponseEntity<?> responseEntity = sessionController.save("Id");
        
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
    }
    
    @Test
    public void participate_shouldReturn_ok() {

        User user = new User();
        user.setId(1L);
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        
        doNothing().when(sessionService).participate(session.getId(), user.getId());

        ResponseEntity<?> responseEntity = sessionController.participate(Long.toString(session.getId()), Long.toString(user.getId()));
        
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }
    
    @Test
    public void participate_with_wrongId_shouldReturn_badRequest() {
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        ResponseEntity<?> responseEntity = sessionController.participate("Id", "userId");
        
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
    }
    
    @Test
    public void noLongerParticipate_shouldReturn_ok() {

        User user = new User();
        user.setId(1L);
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        doNothing().when(sessionService).noLongerParticipate(session.getId(), user.getId());

        ResponseEntity<?> responseEntity = sessionController.noLongerParticipate(Long.toString(session.getId()), Long.toString(user.getId()));
        
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }
    
    @Test
    public void noLongerParticipate_with_wrongId_shouldReturn_badRequest() {
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        ResponseEntity<?> responseEntity = sessionController.noLongerParticipate("Id", "userId");
        
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
    }
}
