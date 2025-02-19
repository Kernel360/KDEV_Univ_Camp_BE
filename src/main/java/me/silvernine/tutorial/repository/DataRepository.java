package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.entity.BatchData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DataRepository extends JpaRepository<BatchData, Long> {
    List<BatchData> findByDate(LocalDate date);
}