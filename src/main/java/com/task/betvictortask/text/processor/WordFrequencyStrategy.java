package com.task.betvictortask.text.processor;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public interface WordFrequencyStrategy {
    Pattern WORD_PATTERN = Pattern.compile("(?i)(?<=^|>|\\s)([a-z]+)(?=[,\\.:\\?]?($|\\s|<))");

    WordFrequencyResult process(List<String> paragraphs);

    default int getLength(List<String> paragraphs) {
        return paragraphs.parallelStream()
                .mapToInt(String::length)
                .sum();
    }

    default String getMostFrequentWord(Map<String, Long> frequencyMap) {
        return frequencyMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}
