package com.example.demo.repository;

import com.example.demo.model.tracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TrackerRepository extends JpaRepository<tracker, Long> {
    Optional<tracker> findByNumber(String number);
}