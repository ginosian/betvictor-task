package com.task.betvictortask.repository;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Entity
@Table(name = "processed_words")
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ProcessedWordsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String freqWord;

    @Column(name = "avg_paragraph_size")
    private Integer avgParagraphSize;

    @Column(name = "avg_paragraph_processing_time")
    private Double avgParagraphProcessingTime;

    @Column(name = "total_processing_time")
    private Double totalProcessingTime;

    private LocalDateTime created;

    private LocalDateTime updated;

    @Column(name = "deleted", columnDefinition = "boolean default false", nullable = false)
    private boolean deleted;

    @PrePersist
    protected void onCreate() {
        created = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = LocalDateTime.now();
    }
}
