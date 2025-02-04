package com.task.betvictortask.text.processor;

import com.task.betvictortask.constants.ParagraphSize;

import java.util.function.BiPredicate;

public record StrategyRule(
        BiPredicate<Integer, ParagraphSize> condition,
        WordFrequencyStrategy strategy) {

    public boolean matches(int paragraphCount, ParagraphSize paragraphSize) {
        return condition.test(paragraphCount, paragraphSize);
    }
}
