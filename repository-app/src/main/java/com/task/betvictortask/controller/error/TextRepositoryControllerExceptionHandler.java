package com.task.betvictortask.controller.error;

import com.task.betvictortask.controller.TextHistoryController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = TextHistoryController.class)
@Slf4j
public class TextRepositoryControllerExceptionHandler {

}
