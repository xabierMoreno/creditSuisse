package com.example.creditSuisse.controller;

import com.example.creditSuisse.controllers.LogController;
import com.example.creditSuisse.entities.Log;
import com.example.creditSuisse.entities.LogLine;
import com.example.creditSuisse.services.LogService;
import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LogControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    LogController controllerUnderTest;

    @Mock
    private LogService logServiceMock;

    @Mock
    private ModelMapper modelMapper;

    @Captor
    ArgumentCaptor<List<LogLine>> argument;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controllerUnderTest).build();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAll() throws Exception {
        Log firstLog = new Log("1",1234L,"test","internet",false);
        Log secondLog = new Log("1",1234L,"test","internet",false);

        when(logServiceMock.findAll()).thenReturn(Arrays.asList(firstLog,secondLog));

        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(status().isOk());

        verify(logServiceMock,  atMost(1)).findAll();
        verifyNoMoreInteractions(logServiceMock);
    }

    @Test
    public void findByAlertTrue() throws Exception{
        Log log = new Log("3",1234L,"test","internet",true);

        when(logServiceMock.getLogsByAlertType(true)).thenReturn(Arrays.asList(log));

        String alert = "true";
        mockMvc.perform(get("/getAlert")
                .param("alert", alert))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());

        verify(logServiceMock,  atMost(1)).getLogsByAlertType(any(Boolean.class));
    }

    @Test
    public void findByAlertFalse() throws Exception{
        Log firstLog = new Log("1",1234L,"test","internet",false);
        Log secondLog = new Log("2",1234L,"test","internet",false);

        when(logServiceMock.getLogsByAlertType(false)).thenReturn(Arrays.asList(firstLog, secondLog));

        String alert = "false";
        mockMvc.perform(get("/getAlert")
                .param("alert", alert))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(status().isOk());

        verify(logServiceMock,  atMost(1)).getLogsByAlertType(any(Boolean.class));
    }

    @Test
    public void findByAlertNoParam() throws Exception{
        mockMvc.perform(get("/getAlert"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void saveLogs() throws Exception {
        mockMvc.perform(put("/"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(logServiceMock).saveLogs( argument.capture());
        List<List<LogLine>> logEvents = argument.getAllValues();
        assertEquals(6, logEvents.get(0).size());
    }
}
