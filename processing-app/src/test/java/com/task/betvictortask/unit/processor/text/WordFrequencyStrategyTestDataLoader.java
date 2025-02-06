package com.task.betvictortask.unit.processor.text;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WordFrequencyStrategyTestDataLoader {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<WordFrequencyStrategyTestData> loadTestData() {
        List<WordFrequencyStrategyTestData> testDataList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            String fileName = String.format("wordfrequencystrategy/text%d.json", i);
            try (InputStream is = WordFrequencyStrategyTestDataLoader.class.getClassLoader().getResourceAsStream(fileName)) {
                if (is != null) {
                    WordFrequencyStrategyTestData testData = objectMapper.readValue(is, WordFrequencyStrategyTestData.class);
                    testDataList.add(testData);
                } else {
                    throw new IOException("Resource not found: " + fileName);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return testDataList;
    }
}
