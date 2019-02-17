package com.example.creditSuisse.services.impl;

import com.example.creditSuisse.entities.Log;
import com.example.creditSuisse.entities.LogDto;
import com.example.creditSuisse.entities.LogLine;
import com.example.creditSuisse.repository.LogRepository;
import com.example.creditSuisse.services.LogService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository  logRepository;

    @Override
    public void saveLogs(List<LogLine> logsList) {

        Map<String, LogLine> collect = getLogsGrouped(logsList);
    
        List<Log> logList = new ArrayList<>();
        for (Map.Entry<String, LogLine> pair : collect.entrySet()) {
            logList.add(mapLogLinesToLog(pair.getValue()));
        }        
        logRepository.saveAll(logList);
    }

    @Override
    public List<Log> findAll() {
        Iterable<Log> logList = logRepository.findAll();
        return Lists.newArrayList(logList);
    }

    @Override
    public List<Log> getLogsByAlertType(boolean alert) {
        List<Log> logList = logRepository.findByAlert(alert);
        return logList;
    }

    private Log mapLogLinesToLog(LogLine logLine) {
        Log log = new Log();
        log.setId(logLine.getId());
        log.setDuration(logLine.getTimeStamp() < 0 ? (-1 * logLine.getTimeStamp()): logLine.getTimeStamp());
        log.setHost(logLine.getHost());
        log.setType(logLine.getType());
        log.setAlert(log.getDuration() > 4 ? true : false);
        return log;
    }

    private Map<String, LogLine> getLogsGrouped(List<LogLine> logsList){
        return logsList.stream().collect(groupingBy(log -> log.getId(), collectingAndThen(reducing(
                (a, b) -> new LogLine(a.getId(), a.getState(), a.getType(), a.getHost(), a.getTimeStamp() - b.getTimeStamp())),
                Optional::get)));
    }
}
