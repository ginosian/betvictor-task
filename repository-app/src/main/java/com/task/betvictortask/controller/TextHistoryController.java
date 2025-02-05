package com.task.betvictortask.controller;

import com.task.betvictortask.controller.dto.TextProcessingStats;
import com.task.betvictortask.repository.ProcessedWordsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@AllArgsConstructor
public class TextHistoryController {

    private final ProcessedWordsService processedWordsService;

    @GetMapping("/betvictor/history")
    public ResponseEntity<List<TextProcessingStats>> getList() {
        return ResponseEntity.ok(processedWordsService.getLast10());
    }
}
