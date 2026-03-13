package com.example.demo.repository;

import com.example.demo.model.Performance1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface Performance1Repository extends JpaRepository<Performance1, Long> {
    @Transactional
    @Modifying
    @Query(value = "ALTER SEQUENCE incidents_id_seq RESTART WITH 1", nativeQuery = true)
    void resetIdSequence();

}