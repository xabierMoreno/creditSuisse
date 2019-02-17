package com.example.creditSuisse.services;

import com.example.creditSuisse.entities.Log;
import com.example.creditSuisse.entities.LogDto;
import com.example.creditSuisse.entities.LogLine;

import java.util.List;

public interface LogService {

    void saveLogs(List<LogLine> logsList);

    List<Log> findAll();

    List<Log> getLogsByAlertType(boolean alert);
}
