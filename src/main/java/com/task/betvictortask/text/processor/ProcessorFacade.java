package com.task.betvictortask.text.processor;

import com.task.betvictortask.aop.ProcessingTimeHolder;
import com.task.betvictortask.constants.ParagraphSize;
import com.task.betvictortask.controller.TextProcessingStats;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class ProcessorFacade {

    private final WordFrequencyStrategySelector strategySelector;

    public TextProcessingStats processText(
            List<String> multiParagraphTexts,
            int gaussLimit,
            ParagraphSize size) {
        log.info("Text processing started with gaussLimit: '{}', paragraphSize: '{}'", gaussLimit, size);
        try {
            int paragraphsAmount = (gaussLimit * (gaussLimit + 1)) / 2;
            StrategyRule strategy = strategySelector.selectStrategy(gaussLimit, size);
            log.info("Selected processing strategy: '{}'", strategy.getClass().getSimpleName());
            WordFrequencyResult processingResult = strategy.strategy().process(multiParagraphTexts);
            Long processTime = ProcessingTimeHolder.getTime();
            log.info("Text processing completed in '{}' ms", processTime);
            return new TextProcessingStats(
                    processingResult.mostFrequentWord(),
                    processingResult.overallLength() / paragraphsAmount,
                    (double) processTime / paragraphsAmount,
                    processTime);
        } finally {
            ProcessingTimeHolder.clear();
        }
    }
}
