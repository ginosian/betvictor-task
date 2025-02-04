package com.task.betvictortask.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProcessedWordsRepository extends JpaRepository<ProcessedWordsEntity, Long> {
    List<ProcessedWordsEntity> findTop10ByOrderByCreatedDesc();
}
