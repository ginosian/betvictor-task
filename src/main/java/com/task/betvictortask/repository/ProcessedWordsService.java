package com.task.betvictortask.repository;

import com.task.betvictortask.controller.TextProcessingStats;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProcessedWordsService {

    private final ProcessedWordsRepository processedWordsRepository;

    public List<TextProcessingStats> getLast10() {
        log.info("Fetching last 10 processed words stats");
        List<TextProcessingStats> statsList = processedWordsRepository.findTop10ByOrderByCreatedDesc()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        log.info("Fetched '{}' records", statsList.size());
        return statsList;
    }

    @Transactional
    public TextProcessingStats save(TextProcessingStats stats) {
        log.info("Saving processed words stats: Most Frequent Word='{}'", stats.getFreqWord());

        ProcessedWordsEntity entity = new ProcessedWordsEntity();
        entity.setFreqWord(stats.getFreqWord());
        entity.setAvgParagraphSize(stats.getAvgParagraphSize());
        entity.setAvgParagraphProcessingTime(stats.getAvgParagraphProcessingTime());
        entity.setTotalProcessingTime(stats.getTotalProcessingTime());

        ProcessedWordsEntity savedEntity = processedWordsRepository.save(entity);
        log.info("Successfully saved processed words stats with ID='{}'", savedEntity.getId());

        return convertToDto(savedEntity);
    }

    //TODO use mapstruct for conversion
    private TextProcessingStats convertToDto(ProcessedWordsEntity entity) {
        return new TextProcessingStats(
                entity.getFreqWord(),
                entity.getAvgParagraphSize(),
                entity.getAvgParagraphProcessingTime(),
                entity.getTotalProcessingTime()
        );
    }
}
