package com.task.betvictortask.text.processor.strategy;

import com.task.betvictortask.aop.Timed;
import com.task.betvictortask.text.processor.WordFrequencyResult;
import com.task.betvictortask.text.processor.WordFrequencyStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;


@Service
@Slf4j
public class SingleThreadedWordFrequency implements WordFrequencyStrategy {

    @Timed
    @Override
    public WordFrequencyResult process(List<String> paragraphs) {
        log.info("Starting single-threaded word frequency processing for {} paragraphs", paragraphs.size());

        int overallLength = getLength(paragraphs);
        log.debug("Total length of paragraphs: '{}'", overallLength);

        Map<String, Long> frequencyMap = new HashMap<>();
        for (String paragraph : paragraphs) {
            Matcher matcher = WORD_PATTERN.matcher(paragraph);
            while (matcher.find()) {
                String word = matcher.group().toLowerCase();
                frequencyMap.put(word, frequencyMap.getOrDefault(word, 0L) + 1);
            }
        }

        log.info("Word single-threaded frequency processing completed successfully");

        String mostFrequentWord = getMostFrequentWord(frequencyMap);
        log.info("Most frequent word: '{}'", mostFrequentWord);

        return new WordFrequencyResult(mostFrequentWord, overallLength);
    }
}

