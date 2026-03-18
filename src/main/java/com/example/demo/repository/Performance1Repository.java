package com.example.demo.repository;

import com.example.demo.model.Performance1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Performance1Repository extends JpaRepository<Performance1, Long> {
}