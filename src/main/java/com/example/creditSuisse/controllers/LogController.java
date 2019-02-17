package com.example.creditSuisse.controllers;

import com.example.creditSuisse.entities.Log;
import com.example.creditSuisse.entities.LogDto;
import com.example.creditSuisse.entities.LogLine;
import com.example.creditSuisse.services.LogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LogController {

    @Autowired
    private LogService logService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(value = "/")
    @ResponseBody
    public List<LogDto> getLogs() {
        List<Log> logList = logService.findAll();
        return logList.stream()
                .map(log -> convertToDto(log))
                .collect(Collectors.toList());
    }

    @PutMapping(value="/")
    @ResponseBody
    public ResponseEntity saveLogs() throws Exception{
        List<LogLine> logDtoList = readLogs();
        logService.saveLogs(logDtoList);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/getAlert")
    @ResponseBody
    public List<LogDto> getLogByAlertType(@RequestParam Boolean alert){
        List<Log> logList = logService.getLogsByAlertType(alert);
        return logList.stream()
                .map(log -> convertToDto(log))
                .collect(Collectors.toList());
    }

    private List<LogLine> readLogs() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        JSONParser parser = new JSONParser();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("json/logs.json").getFile());
        Object objectList = parser.parse(new FileReader(file));
        JSONArray jsonArr = (JSONArray) objectList;
        List<LogLine> logDtoList = new ArrayList<>();
        for(int i=0; i< jsonArr.size(); i++){
            JSONObject object = (JSONObject)jsonArr.get(i);
            LogLine logDto = new LogLine();
            logDto.setId(String.valueOf(object.get("id")));
            logDto.setState(String.valueOf(object.get("state")));
            logDto.setType(String.valueOf(object.get("type")));
            logDto.setHost(String.valueOf(object.get("host")));
            logDto.setTimeStamp(Long.parseLong(String.valueOf(object.get("timeStamp"))));
            logDtoList.add(logDto);
        }
        return logDtoList;
    }

    private LogDto convertToDto(Log post) {
        LogDto logDto = modelMapper.map(post, LogDto.class);
        return logDto;
    }
}
