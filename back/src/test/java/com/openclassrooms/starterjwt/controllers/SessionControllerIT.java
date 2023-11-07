package com.openclassrooms.starterjwt.controllers;

import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.SessionService;
import com.openclassrooms.starterjwt.controllers.SessionController;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = SessionController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class SessionControllerIT {
    
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private SessionService sessionService;
    @Mock
    private SessionMapper sessionMapper;

    Long teacherId = 1L;
    Teacher teacher;
    TeacherDto teacherDto = new TeacherDto();
    Long sessionId = 1L;
    Session session;
    SessionDto sessionDto = new SessionDto();

    @BeforeAll
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

        teacherDto.setId(teacherId);
        teacherDto.setLastName("The");
        teacherDto.setFirstName("Professor");
            
        sessionDto.setId(sessionId);
        sessionDto.setName("Beginners");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("yoga test");
        sessionDto.setTeacher_id(teacher.getId());
    }

    @Test
    public void findById_shouldReturn_sessionDto() throws Exception {

        when(sessionService.getById(session.getId())).thenReturn(session);

        ResultActions response = mockMvc.perform(
            get("/api/session")
            .param("id", "1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(sessionMapper.toDto(session).toString())
        );

        response
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(sessionMapper.toDto(session))));

        verify(sessionService).getById(session.getId());
        verify(session);
    }

    @Test
    public void findAll_shouldReturn_sessionsListDto() throws Exception {

        List<Session> sessions = sessionService.findAll();

        when(sessionService.findAll()).thenReturn(sessions);

        ResultActions response = mockMvc.perform(
            get("/api/session")
            .contentType(MediaType.APPLICATION_JSON)
        );

        response
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(sessionMapper.toDto(sessions))));

        verify(sessionService).findAll();
        verify(sessions);
    }

    @Test
    public void create_shouldReturn_sessionDto() throws Exception {
        
        given(sessionService.create(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));

        // when(sessionService.create(session)).thenReturn(session);

        // sessionDto = sessionMapper.toDto(session);

        ResultActions response = mockMvc.perform(post("/api/session")
            .contentType(MediaType.APPLICATION_JSON)
            .content(sessionMapper.toDto(session).toString())
        );

        response
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(sessionMapper.toDto(session))));

        verify(sessionService).create(session);
        verify(session);
    }

    @Test
    public void update_shouldReturn_sessionDto() throws Exception {

        when(sessionService.update(session.getId(), session)).thenReturn(session);

        sessionDto = sessionMapper.toDto(session);

        ResultActions response = mockMvc.perform(put("/api/session")
            .param("id", "1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(sessionMapper.toDto(session).toString())
        );

        response
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(sessionMapper.toDto(session))));

        verify(sessionService).update(session.getId(), session);
        verify(session);
    }

    @Test
    public void delete_shouldReturn_sessionDto() throws Exception {
        
        doNothing().when(sessionService).delete(session.getId());

        ResultActions response = mockMvc.perform(
            delete("/api/session")
                .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk());

        verify(sessionService).delete(session.getId());
    }

    // @Test
    // public void participate_shouldReturn_sessionDto() {}

    // @Test
    // public void unparticipate_shouldReturn_sessionDto() {}
}
