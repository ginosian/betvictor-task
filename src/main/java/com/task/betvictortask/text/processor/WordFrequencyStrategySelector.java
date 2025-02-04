package com.task.betvictortask.text.processor;

import com.task.betvictortask.constants.ParagraphSize;
import com.task.betvictortask.text.processor.strategy.ForkJoinWordFrequency;
import com.task.betvictortask.text.processor.strategy.ParallelStreamWordFrequency;
import com.task.betvictortask.text.processor.strategy.SingleThreadedWordFrequency;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WordFrequencyStrategySelector {
    private final List<StrategyRule> rules;

    public WordFrequencyStrategySelector(
            SingleThreadedWordFrequency singleThreadedProcessor,
            ParallelStreamWordFrequency parallelStreamProcessor,
            ForkJoinWordFrequency forkJoinProcessor) {

        this.rules = List.of(
                new StrategyRule((count, size) -> count <= 10 && size == ParagraphSize.SHORT, singleThreadedProcessor),
                new StrategyRule((count, size) -> count <= 7 && size == ParagraphSize.MEDIUM, singleThreadedProcessor),
                new StrategyRule((count, size) -> count <= 4 && size == ParagraphSize.LONG, singleThreadedProcessor),
                new StrategyRule((count, size) -> count <= 2 && size == ParagraphSize.VERYLONG, singleThreadedProcessor),

                new StrategyRule((count, size) -> count > 10 && count <= 25 && size == ParagraphSize.MEDIUM, parallelStreamProcessor),
                new StrategyRule((count, size) -> count > 7 && count <= 20 && size == ParagraphSize.MEDIUM, parallelStreamProcessor),
                new StrategyRule((count, size) -> count > 4 && count <= 15 && size == ParagraphSize.LONG, parallelStreamProcessor),
                new StrategyRule((count, size) -> count > 2 && count <= 10 && size == ParagraphSize.VERYLONG, parallelStreamProcessor),

                new StrategyRule((count, size) -> count > 25 || size == ParagraphSize.VERYLONG, forkJoinProcessor),
                new StrategyRule((count, size) -> count > 20 || size == ParagraphSize.VERYLONG, forkJoinProcessor),
                new StrategyRule((count, size) -> count > 15 || size == ParagraphSize.VERYLONG, forkJoinProcessor),
                new StrategyRule((count, size) -> count > 10 || size == ParagraphSize.VERYLONG, forkJoinProcessor)
        );
    }

    public StrategyRule selectStrategy(int paragraphCount, ParagraphSize paragraphSize) {
        return rules.stream()
                .filter(rule -> rule.matches(paragraphCount, paragraphSize))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No strategy found"));
    }
}
