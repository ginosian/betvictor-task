package com.task.betvictortask.controller;

import com.task.betvictortask.client.TextVendorService;
import com.task.betvictortask.constants.ParagraphSize;
import com.task.betvictortask.kafka.KafkaMessage;
import com.task.betvictortask.kafka.KafkaProducerService;
import com.task.betvictortask.repository.ProcessedWordsService;
import com.task.betvictortask.text.processor.ProcessorFacade;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class TextProcessingFacade {

    private final TextVendorService textVendorService;
    private final ProcessorFacade processorFacade;
    private final KafkaProducerService kafkaProducerService;
    private final ProcessedWordsService processedWordsService;

    public TextProcessingStats process(int p, ParagraphSize l) {
        log.info("Starting text processing with paragraphs:'{}' and size:'{}'", p, l);

        List<String> paragraphs = textVendorService.fetchTexts(p, l);
        log.debug("Fetched '{}' paragraphs from TextVendorService", paragraphs.size());

        TextProcessingStats stats = processorFacade.processText(paragraphs, p, l);
        log.info("Processing completed. Most frequent word:'{}', Avg Paragraph Size:'{}', Avg Processing Time:'{}', Total Processing Time:'{}'",
                stats.getFreqWord(), stats.getAvgParagraphSize(), stats.getAvgParagraphProcessingTime(), stats.getTotalProcessingTime());

        kafkaProducerService.sendMessage(new KafkaMessage(
                stats.getFreqWord(),
                stats.getAvgParagraphSize(),
                stats.getAvgParagraphProcessingTime(),
                stats.getTotalProcessingTime()
        ));
        log.info("Sent message to KafkaProducerService");

        processedWordsService.save(stats);
        log.info("Processed words saved to database");

        return stats;
    }
}
