package com.task.betvictortask.unit.processor.text;

import com.task.betvictortask.constants.ParagraphSize;
import com.task.betvictortask.text.processor.StrategyRule;
import com.task.betvictortask.text.processor.WordFrequencyStrategySelector;
import com.task.betvictortask.text.processor.strategy.ForkJoinWordFrequency;
import com.task.betvictortask.text.processor.strategy.ParallelStreamWordFrequency;
import com.task.betvictortask.text.processor.strategy.SingleThreadedWordFrequency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class WordFrequencyStrategySelectorTest {

    @InjectMocks
    private WordFrequencyStrategySelector selector;

    @Mock
    private SingleThreadedWordFrequency singleThreadedProcessor;

    @Mock
    private ParallelStreamWordFrequency parallelStreamProcessor;

    @Mock
    private ForkJoinWordFrequency forkJoinProcessor;

    static Stream<Arguments> provideCombinations() {
        return Stream.of(ParagraphSize.SHORT, ParagraphSize.MEDIUM, ParagraphSize.LONG, ParagraphSize.VERYLONG)
                .flatMap(size -> Stream.iterate(1, i -> i <= 10, i -> i + 1)
                        .map(i -> Arguments.of(i, size)));
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest(name = "{index}. Combination: {0} paragraphs, {1} size")
    @MethodSource("provideCombinations")
    void testSelectStrategy(int paragraphCount, ParagraphSize paragraphSize) {
        StrategyRule selectedRule = selector.selectStrategy(paragraphCount, paragraphSize);
        assertNotNull(selectedRule,
                "No strategy selected for paragraphs: " + paragraphCount + " and size: " + paragraphSize);
        assertNotNull(selectedRule.strategy(),
                "Strategy object is null for paragraphs: " + paragraphCount + " and size: " + paragraphSize);
    }
}