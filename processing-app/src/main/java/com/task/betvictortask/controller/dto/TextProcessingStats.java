package com.task.betvictortask.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TextProcessingStats {
    @JsonProperty("freq_word")
    private String freqWord;

    @JsonProperty("avg_paragraph_size")
    private int avgParagraphSize;

    @JsonProperty("avg_paragraph_processing_time")
    private double avgParagraphProcessingTime;

    @JsonProperty("total_processing_time")
    private double totalProcessingTime;
}
