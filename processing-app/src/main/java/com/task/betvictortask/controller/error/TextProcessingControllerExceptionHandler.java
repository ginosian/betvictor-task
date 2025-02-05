package com.task.betvictortask.controller.error;

import com.task.betvictortask.constants.ParagraphSize;
import com.task.betvictortask.controller.TextProcessingController;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

import static com.task.betvictortask.controller.error.ErrorCodes.PV1;
import static com.task.betvictortask.controller.error.ErrorCodes.PV2;

@RestControllerAdvice(assignableTypes = TextProcessingController.class)
@Slf4j
public class TextProcessingControllerExceptionHandler {

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            TypeMismatchException ex) {
        ApiError error = new ApiError(
                PV1.name(),
                String.format(
                        PV1.getMessage(),
                        ex.getValue(),
                        ex.getPropertyName(),
                        Arrays.toString(ParagraphSize.values())));
        ErrorResponse dto = new ErrorResponse();
        dto.addError(error);
        log.error("Handling TypeMismatchException: value='{}', property='{}'", ex.getValue(), ex.getPropertyName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(dto);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        ApiError error = new ApiError(PV2.name(), PV2.getMessage());
        ErrorResponse dto = new ErrorResponse();
        dto.addError(error);
        log.error("Handling ConstraintViolationException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(dto);
    }
}
