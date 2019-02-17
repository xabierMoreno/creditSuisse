package com.example.creditSuisse.services.impl;

import com.example.creditSuisse.entities.Log;
import com.example.creditSuisse.entities.LogLine;
import com.example.creditSuisse.repository.LogRepository;
import com.example.creditSuisse.services.LogService;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.*;

@Service
public class LogServiceImpl implements LogService {

    private static final Logger LOGGER = Logger.getLogger(LogServiceImpl.class);

    @Autowired
    private LogRepository  logRepository;

    @Override
    public void saveLogs(List<LogLine> logsList) {
        LOGGER.debug("Begin Save Logs");
        Map<String, LogLine> collect = getLogsGrouped(logsList);
    
        List<Log> logList = new ArrayList<>();
        for (Map.Entry<String, LogLine> pair : collect.entrySet()) {
            logList.add(mapLogLinesToLog(pair.getValue()));
        }        
        logRepository.saveAll(logList);
        LOGGER.debug("Save Logs Correctly");
    }

    @Override
    public List<Log> findAll() {
        LOGGER.debug("Begin Find All Logs");
        Iterable<Log> logList = logRepository.findAll();
        LOGGER.debug("Find All Logs Correctly");
        return Lists.newArrayList(logList);
    }

    @Override
    public List<Log> getLogsByAlertType(boolean alert) {
        LOGGER.debug("Find Logs By Alert" + alert);
        List<Log> logList = logRepository.findByAlert(alert);
        LOGGER.debug("Find Logs By Alert" + alert + "correct");
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
