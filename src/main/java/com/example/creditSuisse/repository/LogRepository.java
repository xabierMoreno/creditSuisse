package com.example.creditSuisse.repository;

import com.example.creditSuisse.entities.Log;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends CrudRepository<Log, String> {}