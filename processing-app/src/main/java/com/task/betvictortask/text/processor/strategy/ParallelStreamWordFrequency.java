package com.task.betvictortask.text.processor.strategy;


import com.task.betvictortask.aop.Timed;
import com.task.betvictortask.text.processor.WordFrequencyResult;
import com.task.betvictortask.text.processor.WordFrequencyStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ParallelStreamWordFrequency implements WordFrequencyStrategy {

    @Timed
    @Override
    public WordFrequencyResult process(List<String> paragraphs) {
        log.info("Starting parallel word frequency processing for '{}' paragraphs", paragraphs.size());

        int overallLength = getLength(paragraphs);
        log.debug("Total length of paragraphs: '{}'", overallLength);

        ConcurrentMap<String, Long> frequencyMap = paragraphs.parallelStream()
                .flatMap(this::findMatches)
                .collect(Collectors.groupingByConcurrent(Function.identity(), Collectors.counting()));

        log.info("Word parallel frequency processing completed successfully");

        String mostFrequentWord = getMostFrequentWord(frequencyMap);
        log.info("Most frequent word: '{}'", mostFrequentWord);

        return new WordFrequencyResult(mostFrequentWord, overallLength);
    }
}
