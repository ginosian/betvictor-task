package com.task.betvictortask.text.processor.strategy;

import com.task.betvictortask.aop.Timed;
import com.task.betvictortask.text.processor.WordFrequencyResult;
import com.task.betvictortask.text.processor.WordFrequencyStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ForkJoinWordFrequency implements WordFrequencyStrategy {

    @Timed
    @Override
    public WordFrequencyResult process(List<String> paragraphs) {
        log.info("Starting ForkAndJoin word frequency processing for '{}' paragraphs", paragraphs.size());

        int overallLength = getLength(paragraphs);
        log.debug("Total length of paragraphs: '{}'", overallLength);

        ForkJoinPool customPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        ConcurrentMap<String, Long> frequencyMap;
        try {
            frequencyMap = customPool.submit(() ->
                    paragraphs.parallelStream()
                            .flatMap(paragraph -> {
                                Matcher matcher = WORD_PATTERN.matcher(paragraph);
                                return matcher.results().map(matchResult -> matchResult.group().toLowerCase());
                            })
                            .collect(Collectors.groupingByConcurrent(Function.identity(), Collectors.counting()))
            ).get();
            log.info("Word frequency processing completed successfully");
        } catch (Exception e) {
            log.error("Error processing paragraphs", e);
            throw new RuntimeException("Error processing paragraphs", e);
        } finally {
            customPool.shutdown();
            log.debug("Custom ForkJoinPool shutdown");
        }

        String mostFrequentWord = getMostFrequentWord(frequencyMap);
        log.info("Most frequent word: '{}'", mostFrequentWord);

        return new WordFrequencyResult(mostFrequentWord, overallLength);
    }
}
