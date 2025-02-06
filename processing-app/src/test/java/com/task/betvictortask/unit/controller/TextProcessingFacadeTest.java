package com.task.betvictortask.unit.controller;

import com.task.betvictortask.client.TextVendorService;
import com.task.betvictortask.constants.ParagraphSize;
import com.task.betvictortask.controller.TextProcessingFacade;
import com.task.betvictortask.controller.dto.TextProcessingStats;
import com.task.betvictortask.kafka.KafkaMessage;
import com.task.betvictortask.kafka.KafkaProducerService;
import com.task.betvictortask.text.processor.ProcessorFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TextProcessingFacadeTest {

    @Mock
    private TextVendorService textVendorService;

    @Mock
    private ProcessorFacade processorFacade;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private TextProcessingFacade textProcessingFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcess() {
        int paragraphCount = 5;
        ParagraphSize paragraphSize = ParagraphSize.MEDIUM;
        List<String> mockParagraphs = List.of("Paragraph 1", "Paragraph 2", "Paragraph 3");
        TextProcessingStats mockStats = new TextProcessingStats("testWord", 10, 2.3, 11.5);

        when(textVendorService.fetchTexts(paragraphCount, paragraphSize)).thenReturn(mockParagraphs);
        when(processorFacade.processText(mockParagraphs, paragraphCount, paragraphSize)).thenReturn(mockStats);

        TextProcessingStats result = textProcessingFacade.process(paragraphCount, paragraphSize);

        assertEquals(mockStats, result);
        verify(textVendorService, times(1)).fetchTexts(paragraphCount, paragraphSize);
        verify(processorFacade, times(1)).processText(mockParagraphs, paragraphCount, paragraphSize);
        verify(kafkaProducerService, times(1)).sendMessage(any(KafkaMessage.class));
    }
}
