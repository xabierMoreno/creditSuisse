package com.example.creditSuisse.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customers")
public class Log {

    @Id
    private String id;
    private String duration;
    private String type;
    private String host;
    private boolean alert;

}