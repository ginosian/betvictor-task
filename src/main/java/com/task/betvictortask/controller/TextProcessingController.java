package com.task.betvictortask.controller;


import com.task.betvictortask.constants.ParagraphSize;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@AllArgsConstructor
public class TextProcessingController {

    private final TextProcessingFacade textProcessingFacade;

    @GetMapping("/betvictor/text")
    public ResponseEntity<TextProcessingStats> process(
            @RequestParam(name = "p")
            @Min(value = 1)
            @Max(value = 10)
            int p,
            @RequestParam(name = "l") ParagraphSize l) {
        return ResponseEntity.ok(textProcessingFacade.process(p, l));
    }
}
