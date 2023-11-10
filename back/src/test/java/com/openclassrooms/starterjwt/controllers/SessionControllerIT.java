package com.openclassrooms.starterjwt.controllers;

import java.util.Date;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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

    @InjectMocks
    private SessionController sessionController;

    Long teacherId = 1L;
    Teacher teacher;
    Long sessionId = 1L;
    Session session;

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
    }

    @Test
    public void findById_shouldReturn_sessionDto() throws Exception {

        when(sessionService.getById(session.getId())).thenReturn(session);

        sessionController.findById(Long.toString(session.getId()));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
            .get("/api/session/{id}")
                .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sessionMapper.toDto(session).toString())
        );

        response
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(sessionMapper.toDto(session))));
    }

    @Test
    public void findAll_shouldReturn_sessionsListDto() throws Exception {

        List<Session> sessions = sessionService.findAll();

        when(sessionService.findAll()).thenReturn(sessions);

        sessionController.findAll();

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
            .get("/api/session")
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

        sessionController.create(sessionMapper.toDto(session));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
            .post("/api/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sessionMapper.toDto(session).toString())
        );

        response
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(sessionMapper.toDto(session))));
    }

    @Test
    public void update_shouldReturn_sessionDto() throws Exception {

        when(sessionService.update(session.getId(), session)).thenReturn(session);

        sessionController.update(Long.toString(session.getId()), sessionMapper.toDto(session));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
            .put("/api/session/{id}")
                .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sessionMapper.toDto(session).toString())
        );

        response
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(sessionMapper.toDto(session))));
    }

    @Test
    public void delete_shouldReturn_sessionDto() throws Exception {
        
        doNothing().when(sessionService).delete(session.getId());

        sessionController.save(Long.toString(session.getId()));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
            .delete("/api/session/{id}")
                .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk());

        verify(sessionService).delete(session.getId());
    }

    @Test
    public void participate_shouldReturn_sessionDto() throws Exception {
        
        doNothing().when(sessionService).participate(session.getId(), 1L);

        sessionController.participate(Long.toString(session.getId()), "1");

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
            .post("/api/session/{id}/participate/{userId}")
                .param("id", "1")
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk());

        verify(sessionService).participate(session.getId(), 1L);
    }

    @Test
    public void unparticipate_shouldReturn_sessionDto() throws Exception {
        
        doNothing().when(sessionService).noLongerParticipate(session.getId(), 1L);

        sessionController.noLongerParticipate(Long.toString(session.getId()), "1");

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
            .delete("/api/session/{id}/participate/{userId}")
                .param("id", "1")
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk());

        verify(sessionService).noLongerParticipate(session.getId(), 1L);
    }
}
