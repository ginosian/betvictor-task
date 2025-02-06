package com.task.betvictortask.text.processor;

import com.task.betvictortask.text.processor.strategy.ForkJoinWordFrequency;
import com.task.betvictortask.text.processor.strategy.ParallelStreamWordFrequency;
import com.task.betvictortask.text.processor.strategy.SingleThreadedWordFrequency;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WordFrequencyStrategyTest {

    private final ForkJoinWordFrequency forkJoinStrategy = new ForkJoinWordFrequency();
    private final ParallelStreamWordFrequency parallelStreamStrategy = new ParallelStreamWordFrequency();
    private final SingleThreadedWordFrequency singleThreadedStrategy = new SingleThreadedWordFrequency();

    public static List<WordFrequencyStrategyTestData> testDataProvider() {
        return WordFrequencyStrategyTestDataLoader.loadTestData();
    }

    @ParameterizedTest(name = "ForkJoin Strategy - {index}: {0}")
    @MethodSource("testDataProvider")
    void testForkJoinStrategy(WordFrequencyStrategyTestData testData) {
        testStrategy(forkJoinStrategy, testData);
    }

    @ParameterizedTest(name = "ParallelStream Strategy - {index}: {0}")
    @MethodSource("testDataProvider")
    void testParallelStreamStrategy(WordFrequencyStrategyTestData testData) {
        testStrategy(parallelStreamStrategy, testData);
    }

    @ParameterizedTest(name = "SingleThreaded Strategy - {index}: {0}")
    @MethodSource("testDataProvider")
    void testSingleThreadedStrategy(WordFrequencyStrategyTestData testData) {
        testStrategy(singleThreadedStrategy, testData);
    }

    private void testStrategy(WordFrequencyStrategy strategy, WordFrequencyStrategyTestData testData) {
        List<String> paragraphs = List.of(testData.getText());
        WordFrequencyResult result = strategy.process(paragraphs);
        assertEquals(testData.getMostFrequentWord(), result.mostFrequentWord());
    }
}