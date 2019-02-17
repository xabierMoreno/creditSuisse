package com.example.creditSuisse.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LogLine {

    private String id;
    private String state;
    private String type;
    private String host;
    private Long timeStamp;
}