package com.task.betvictortask.text.processor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(of = {"testCase"})
public class WordFrequencyStrategyTestData {
    private String text;
    private String mostFrequentWord;
    private String testCase;
}
